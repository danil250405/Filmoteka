package org.glazweq.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MovieCard {

    @JsonProperty("name")
    private String name;
    @JsonProperty("previewUrl")
    private String previewUrlImg;
    @JsonProperty("kinopoiskRating")
    private double kinopoiskRating;
    @JsonProperty("description")
    private String description ;
    @JsonProperty("imdbRating")
    private double imdbRating;
    @JsonProperty("year")
    private int year;
    @Setter
    @Id
    @JsonProperty("id")
    private int id;

    public MovieCard(int id, String name, String previewUrlImg, double kinopoiskRating, double imdbRating, String description, int year) {
        this.id = id;
        this.name = name;
        this.previewUrlImg = previewUrlImg;
        this.kinopoiskRating = kinopoiskRating;
        this.description = description;
        this.imdbRating = imdbRating;
        this.year = year;
    }

    public MovieCard() {
    }


}
