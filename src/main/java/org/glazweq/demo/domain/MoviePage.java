package org.glazweq.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
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
    @JsonProperty("year")
    private int year;
    @Setter
    @Id
    @JsonProperty("id")
    private int id;

    public MoviePage(int id, String name, String previewUrlImg, double kinopoiskRating, double imdbRating, String description, int year, String backdrop) {
        this.id = id;
        this.name = name;
        this.previewUrlImg = previewUrlImg;
        this.kinopoiskRating = kinopoiskRating;
        this.description = description;
        this.imdbRating = imdbRating;
        this.year = year;
        this.backdrop = backdrop;
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
