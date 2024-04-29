package org.glazweq.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.glazweq.demo.domain.MovieCard;
import org.glazweq.demo.domain.SearchMovie;
import org.glazweq.demo.service.FiltersMovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    private final FiltersMovieService filtersMovieService;

    public SearchController(FiltersMovieService filtersMovieService) {
        this.filtersMovieService = filtersMovieService;
    }

    @GetMapping("/find-movie")
    public ResponseEntity<List<SearchMovie>> findMovies(@RequestParam String movieName) throws JsonProcessingException {
        List<SearchMovie> movies = filtersMovieService.findMoviesByText(movieName);
        System.out.println("in find movies");
//        for (SearchMovie m : movies){
////            System.out.println(m.ur);
//        }
        return ResponseEntity.ok(movies);
    }
}
