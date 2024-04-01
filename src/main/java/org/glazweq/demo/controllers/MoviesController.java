package org.glazweq.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.glazweq.demo.domain.MovieCard;

import org.glazweq.demo.service.FilmApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller

public class MoviesController {
    public final int productPerPage = 24;
    private final FilmApiService filmApiService;

    //requests
    private final String defaultRequest ="&selectFields=id&selectFields=enName&selectFields=year&selectFields=rating&selectFields=poster&notNullFields=enName&notNullFields=poster.url&selectFields=votes&sortField=votes.imdb&sortType=-1";

    @Autowired
    public MoviesController(FilmApiService filmApiService) throws JsonProcessingException {
        this.filmApiService = filmApiService;
    }
    @GetMapping("/")
    public String redirectToHomePage() {
        return "redirect:/home-page";
    }



    @Cacheable(value = "totalFilmsInApiCache")
    public int getTotalFilmsInApi() throws JsonProcessingException {
        return filmApiService.totalFilmInApi();
    }
    @GetMapping("/home-page")
    public String getCardsList(Model model, @RequestParam(defaultValue = "1") int page, HttpServletRequest request)
            throws JsonProcessingException {

        // Получение значения параметра genre из URL
        String genre = request.getParameter("genre");
        if (genre == null || genre.isEmpty()) {
            System.out.println("finito3");
            genre = "any"; // Значение по умолчанию, если параметр не задан
        }
        model.addAttribute("selectedGenre", genre);
        int totalFilmsInApi = filmApiService.totalFilmInApi();
        int pagesAmount = getTotalFilmsInApi() / productPerPage;

        // list with genres
        List<String> genres = Arrays.asList(
                "any", "action", "adventure", "adult", "anime", "biography", "cartoon", "ceremony", "children's", "comedy", "concert", "crime",
                "detective", "documentary", "drama", "family", "fantasy", "film noir", "game", "history", "horror", "melodrama", "music",
                "musical", "news", "reality TV", "short film", "sport", "talk show", "thriller", "war", "western");
        model.addAttribute("genres", genres);
        String requestUrl;
        if (genre == null || genre.equals("anyGenre")) {
            // Если жанр не задан или равен "all", получаем список всех фильмов без фильтрации
            requestUrl = filmApiService.getUrlForApi(page, productPerPage, null);
            System.out.println("finito1");
        } else {
            // Иначе, получаем список фильмов с фильтрацией по жанру
            requestUrl = filmApiService.getUrlForApi(page, productPerPage, genre);
            System.out.println("notfinito1");
        }


        List<MovieCard> movies = filmApiService.getMoviesList(requestUrl);


        // list with films
        model.addAttribute("movies", movies);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String roleAuthUser;
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            roleAuthUser = "admin";
        } else {
            roleAuthUser = "guest";
        }
        model.addAttribute("userRole", roleAuthUser);

        String baseUrl;
        // for pagination
        if (genre.equals("any")) {
            System.out.println("finito3wwwwwwwwwwwwwwww");

            baseUrl = "/home-page?"+ "page=";
        }
        else {
            baseUrl = "/home-page?genre=" + genre + "&page=";
        }
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalItems", totalFilmsInApi);
        model.addAttribute("pageSize", productPerPage);
        model.addAttribute("totalPages", pagesAmount); // max page
        return "home-page";
    }
    @PostMapping("/home-page")
    public String handleGenreSelection( @RequestParam(defaultValue = "1") int page, @RequestParam("genre") String selectedGenre,  Model model) throws JsonProcessingException {
        System.out.println("qqqqqqqqqqqqqq"+ selectedGenre);
        int totalFilmsInApi = filmApiService.totalFilmInApi();
        int pagesAmount = getTotalFilmsInApi()/productPerPage;



        //list with genres
        List<String> genres = Arrays.asList(
                "any", "action", "adventure", "adult", "anime", "biography", "cartoon", "ceremony", "children's", "comedy", "concert", "crime",
                "detective", "documentary", "drama", "family", "fantasy", "film noir", "game", "history", "horror", "melodrama", "music",
                "musical", "news", "reality TV", "short film", "sport", "talk show", "thriller", "war", "western");

        model.addAttribute("genres", genres);
    model.addAttribute("selectedGenre", selectedGenre);
       String requestUrl = filmApiService.getUrlForApi(page, productPerPage, selectedGenre);

        List<MovieCard> movies = filmApiService.getMoviesList(requestUrl);
        //list with films
        model.addAttribute("movies", movies);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String roleAuthUser;
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            roleAuthUser = "admin";
        }
        else {
            roleAuthUser = "guest";
        }
        model.addAttribute("userRole", roleAuthUser);
        //for pagination
        model.addAttribute("currentPage", page);
        model.addAttribute("totalItems", totalFilmsInApi);
        model.addAttribute("pageSize", productPerPage);
        model.addAttribute("totalPages", pagesAmount);//max page
        return "redirect:/home-page?genre=" + selectedGenre;


    }
//    filter
//    @PostMapping("/filter")
//    public String filterMovies(@RequestParam("genre") String selectedGenre, Model model){
//        String urlResponse = filmApiService.getUrlForApi()
//        return "home-page";
//    }
}
