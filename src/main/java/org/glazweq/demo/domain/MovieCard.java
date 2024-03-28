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

    @JsonProperty("enName")
    private String enName;

    @JsonProperty("previewUrl")
    private String previewUrlImg;


    public MovieCard( String enName, String previewUrlImg) {

        this.enName = enName;

        this.previewUrlImg = previewUrlImg;
    }
}
