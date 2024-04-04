package org.glazweq.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter

public class MovieCard {

    @JsonProperty("name")
    private String name;

    @JsonProperty("previewUrl")
    private String previewUrlImg;
    @JsonProperty("kinopoiskRating")
    private String kinopoiskRating;
    @JsonProperty("description")
    private String description ;
    @JsonProperty("imdbRating")
    private String imdbRating;
    @JsonProperty("year")
    private String year;
    @JsonProperty("id")
    private int id;

    public MovieCard(int id, String name, String previewUrlImg, String kinopoiskRating, String imdbRating, String description, String year) {
        this.id = id;
        this.name = name;
        this.previewUrlImg = previewUrlImg;
        this.kinopoiskRating = kinopoiskRating;
        this.description = description;
        this.imdbRating = imdbRating;
        this.year = year;
    }
}
