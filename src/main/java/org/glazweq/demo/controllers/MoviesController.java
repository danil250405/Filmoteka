package org.glazweq.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.glazweq.demo.domain.MovieCard;

import org.glazweq.demo.service.FilmApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller

public class MoviesController {
    public final int productPerPage = 24;
    private final FilmApiService filmApiService;
    @Autowired
    public MoviesController(FilmApiService filmApiService) throws JsonProcessingException {
        this.filmApiService = filmApiService;
    }


    @GetMapping("/home-page")
    public String getCardsList(Model model, @RequestParam(defaultValue = "1") int page)
            throws JsonProcessingException {
        int totalFilmsInApi = filmApiService.totalFilmInApi();
        int pagesAmount = totalFilmsInApi/productPerPage; // 181

        List<MovieCard> movies = filmApiService.getMoviesList(page, productPerPage);
        //list with films
        model.addAttribute("movies", movies);


        //for pagination
        model.addAttribute("currentPage", page);
        model.addAttribute("totalItems", totalFilmsInApi);
        model.addAttribute("pageSize", productPerPage);
        model.addAttribute("totalPages", pagesAmount);//max page
        return "home-page";
    }

}
