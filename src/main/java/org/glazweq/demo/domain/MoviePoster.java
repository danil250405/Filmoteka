package org.glazweq.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoviePoster {
    @JsonProperty("previewUrl")
    private String previewUrlImg;

    public MoviePoster(String previewUrlImg) {
        this.previewUrlImg = previewUrlImg;
    }
}
