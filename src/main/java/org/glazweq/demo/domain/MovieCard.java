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
    @JsonProperty("type")
    private String type;



    public MovieCard( String name, String type) {

        this.name = name;
        this.type = type;
    }
}
