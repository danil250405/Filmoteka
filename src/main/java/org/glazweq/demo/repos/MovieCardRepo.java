package org.glazweq.demo.repos;

import org.glazweq.demo.domain.MovieCard;
import org.glazweq.demo.domain.Poster;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieCardRepo extends CrudRepository<MovieCard, Long> {
    List<MovieCard> findByGenresName(String genreName);
}
