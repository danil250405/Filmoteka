package org.glazweq.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManFromMovie {
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("photo")
    private String photo;
    @JsonProperty("profession")
    private String profession;
    public ManFromMovie(){}
    public ManFromMovie(int id, String name, String photo, String profession) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.profession = profession;
    }
}
