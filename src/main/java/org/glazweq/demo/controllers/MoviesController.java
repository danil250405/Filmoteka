package org.glazweq.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.glazweq.demo.domain.MovieCard;
import org.glazweq.demo.service.FilmApiService;
import org.glazweq.demo.service.JsonDecoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller

public class MoviesController {
    private final FilmApiService filmApiService;
    @Autowired
    public MoviesController(FilmApiService filmApiService) throws JsonProcessingException {
        this.filmApiService = filmApiService;
    }

    @GetMapping("/home-page")
    public String getCardsList(Model model) throws JsonProcessingException {

        List<MovieCard> movies = filmApiService.getTenMoviesList(1);
        model.addAttribute("movies", movies);
        return "home-page";
    }
    @GetMapping("/")
    public String showTenMovies() throws JsonProcessingException {

    return "qwe";
}
}
