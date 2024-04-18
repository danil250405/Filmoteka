package org.glazweq.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reviewText;
    private double reviewScore;
    private Date date;
    @Column(name = "movie_id")
    private int movieId; // Идентификатор фильма, к которому относится отзыв

}
