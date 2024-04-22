package org.glazweq.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class MoviePage {

    @JsonProperty("name")
    private String name;

    @JsonProperty("previewUrl")
    private String previewUrlImg;
    @JsonProperty("kinopoiskRating")
    private double kinopoiskRating;
    @JsonProperty("backdrop")
    private String backdrop ;
    @JsonProperty("description")
    private String description ;
    @JsonProperty("imdbRating")
    private double imdbRating;
    private double streamVibeRating;
    @JsonProperty("year")
    private int year;
    @JsonProperty("shortDescription")
    private String shortDescription;
    @Setter
    @Id
    @JsonProperty("id")
    private int id;
    private List<ManFromMovie> manFromMovieList;
    private List<String> genres;

    public MoviePage(int id, String name, String previewUrlImg, double kinopoiskRating,
                     double imdbRating, String description, int year, String backdrop,
                     String shortDescription, List<ManFromMovie> manFromMovieList) {
        this.id = id;
        this.name = name;
        this.previewUrlImg = previewUrlImg;
        this.kinopoiskRating = Math.round(kinopoiskRating * 10.0) / 10.0;
        this.description = description;
        this.imdbRating = imdbRating;
        this.year = year;
        this.backdrop = backdrop;
        this.shortDescription = shortDescription;
        this.manFromMovieList = manFromMovieList;
    }
    public MoviePage() {
    }
    @Override
    public String toString() {
        return "MoviePage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", previewImg='" + previewUrlImg + '\'' +
                ", ratingKinopoisk=" + kinopoiskRating +
                ", ratingImdb=" + imdbRating +
                ", description='" + description + '\'' +
                ", releaseYear=" + year +
                '}';
    }

}
