package org.glazweq.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Table(name = "review")
@Entity
public class Review implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reviewText;
    private double reviewScore;
    private Date date;
    @Column(name = "movie_id")
    private int movieId; // Идентификатор фильма, к которому относится отзыв

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Review(String reviewText, double reviewScore, Date date, int movieId, User user) {
        this.reviewText = reviewText;
        this.reviewScore = reviewScore;
        this.date = date;
        this.movieId = movieId;
        this.user = user;
    }

    public Review() {

    }
}
