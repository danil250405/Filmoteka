package org.glazweq.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.util.ArrayList;
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
    public int getTotalFilmsInApi(JsonNode jsonNode) throws JsonProcessingException {
        return filmApiService.getTotalFilmInApi(jsonNode);
    }
    @PostMapping("/home-page")
    public String postHomePage(@RequestParam(defaultValue = "1") int page, @RequestParam("genre") String selectedGenre,
                              @RequestParam("yearRange") String selectedYearRange,  Model model){

        return "redirect:/home-page?genre=" + selectedGenre + "&year=" + selectedYearRange;

    }


    @GetMapping("/home-page")

    public String getCardsList(Model model, @RequestParam(defaultValue = "1") int page, HttpServletRequest request)
            throws JsonProcessingException {
        System.out.println("now works getCardsList");
        // Получение значения параметра genre из URL
        String selectedGenre = request.getParameter("genre");
        String selectedYearRange = request.getParameter("year");

        // проверка если наши фильтры не заданы то будут равны нулю
        if (selectedGenre == null || selectedGenre.isEmpty()) {
            selectedGenre = "any"; // Значение по умолчанию, если параметр не задан
        }
        if (selectedYearRange == null || selectedYearRange.isEmpty()) {
            selectedYearRange = "any"; // Значение по умолчанию, если параметр не задан
        }

        model.addAttribute("selectedYearRange", selectedYearRange);
        model.addAttribute("selectedGenre", selectedGenre);


        // list with genres
        List<String> genres = Arrays.asList(
                "any", "action", "adventure", "adult", "anime", "biography", "cartoon", "ceremony", "children's", "comedy", "concert", "crime",
                "detective", "documentary", "drama", "family", "fantasy", "film noir", "game", "history", "horror", "melodrama", "music",
                "musical", "news", "reality TV", "short film", "sport", "talk show", "thriller", "war", "western");
        model.addAttribute("genres", genres);

        //range year
        List<String> yearRanges = new ArrayList<>();
        yearRanges.add("any");
        for (int year = 1960; year <= 2020; year += 10) {
            int endYear = year + 9;
            yearRanges.add(year + "-" + endYear);
        }
        model.addAttribute("yearRanges", yearRanges);

        String requestUrl;
        if ((selectedGenre == null || selectedGenre.equals("any")) && (selectedYearRange == null || selectedYearRange.equals("any"))) {
            // Если ни жанр, ни диапазон лет не заданы, получаем список всех фильмов без фильтрации
            requestUrl = filmApiService.getUrlForApi(page, productPerPage, null, null);
            System.out.println("1");
        } else if ((selectedGenre != null || !selectedGenre.equals("any")) && (selectedYearRange == null || selectedYearRange.equals("any"))) {
            // Если задан жанр, но не задан диапазон лет
            requestUrl = filmApiService.getUrlForApi(page, productPerPage, selectedGenre, null);
            System.out.println("2");
        } else if ((selectedGenre == null || selectedGenre.equals("any")) && (selectedYearRange != null || !selectedYearRange.equals("any"))) {
            // Если не задан жанр, но задан диапазон лет
            requestUrl = filmApiService.getUrlForApi(page, productPerPage, null, selectedYearRange);
            System.out.println("3");
        } else {
            // Если заданы и жанр, и диапазон лет
            requestUrl = filmApiService.getUrlForApi(page, productPerPage, selectedGenre, selectedYearRange);
            System.out.println("4");
        }

        //take full json from api
        JsonNode responseJson = filmApiService.getResponseFromApi(requestUrl);
        //take just branch total = how many films by this request in the api
        int totalFilmsInApi = getTotalFilmsInApi(responseJson);
        int pagesAmount = totalFilmsInApi / productPerPage;



        //take just branch docs = take card with movie
        List<MovieCard> movies = filmApiService.getMoviesList(responseJson);

        // list with films
        model.addAttribute("movies", movies);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //just check role user
        String roleAuthUser;
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            roleAuthUser = "admin";
        } else {
            roleAuthUser = "guest";
        }
        model.addAttribute("userRole", roleAuthUser);

        //generate url
        String baseUrl = "/home-page?";
        if (!selectedGenre.equals("any")) {
            baseUrl += "genre=" + selectedGenre + "&";
        }
        if (!selectedYearRange.equals("any")) {
            baseUrl += "year=" + selectedYearRange + "&";
        }
        baseUrl += "page=";

        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalItems", totalFilmsInApi);
        model.addAttribute("pageSize", productPerPage);
        model.addAttribute("totalPages", pagesAmount); // max page
        return "home-page";
    }
}
