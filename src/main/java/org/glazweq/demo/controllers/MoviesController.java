package org.glazweq.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.glazweq.demo.domain.MovieCard;
import org.glazweq.demo.service.FilmApiService;
import org.glazweq.demo.service.JsonDecoderService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class MoviesController {
    private final FilmApiService filmApiService;
    @GetMapping("/home-page")
    public String getHomePage(){
        return "home-page";
    }

    @GetMapping("/")
    public String showTenMovies() throws JsonProcessingException {

    ObjectMapper mapper = new ObjectMapper();
    String filmId = "535341";
    String jsonSource = filmApiService.getFirstTenMovie(filmId);
    JsonNode node = JsonDecoderService.parse(jsonSource);
        String name = node.get("name").asText();
        String type = node.get("type").asText();
    MovieCard movieCard = new MovieCard(name, type);
   // MovieCard movieCard = mapper.treeToValue(node, MovieCard.class);
    //String movieName = movieCard.getName();
    //System.out.println(movieName);
//        JsonNode typeNode = node.get("  ");
//    if (typeNode != null) {
//        System.out.println(typeNode.asText());
//    } else {
//        System.out.println("Ключ \"type\" не найден в JSON");
        System.out.println(movieCard.getName() + " " + movieCard.getType());
    return "qwe";
}
}
