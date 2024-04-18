package org.glazweq.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Poster implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column()
    private int filmId;
    @Column()
    private String posterUrl;
    @Column()
    private String keyword;
    @Column()
    private String enKeyword;
    public Poster(int filmId, String posterUrl, String keyword, String enKeyword) {
        this.filmId = filmId;
        this.posterUrl = posterUrl;
        this.keyword = keyword;
        this.enKeyword = enKeyword;
    }

    // Getters, setters, constructors
}

