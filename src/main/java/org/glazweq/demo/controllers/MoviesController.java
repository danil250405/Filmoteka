package org.glazweq.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.glazweq.demo.domain.MovieCard;

import org.glazweq.demo.service.FilmApiService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/home-page")
    public String getCardsList(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = defaultRequest) String requestUrl)
            throws JsonProcessingException {
        int totalFilmsInApi = filmApiService.totalFilmInApi();
        int pagesAmount = totalFilmsInApi/productPerPage; // 181

        //list with genres
        List<String> genres = Arrays.asList(
                "action", "adventure", "adult", "anime", "biography", "cartoon", "ceremony", "children's", "comedy", "concert", "crime",
                "detective", "documentary", "drama", "family", "fantasy", "film noir", "game", "history", "horror", "melodrama", "music",
                "musical", "news", "reality TV", "short film", "sport", "talk show", "thriller", "war", "western");

        model.addAttribute("genres", genres);


        List<MovieCard> movies = filmApiService.getMoviesList(page, productPerPage, requestUrl);
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
        return "home-page";


    }

////    filter
//    @PostMapping("/filter")
//    public String filterMovies(@RequestParam("genre") String selectedGenre, Model model){
//
//
//    }
}
