package org.glazweq.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchMovie {
    @Id
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;

    @JsonProperty("previewUrl")
    private String previewUrl;
}
