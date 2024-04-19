package org.glazweq.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Please Enter Your Score")
    @Min(value = 0)
    @Max(value = 10)
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
