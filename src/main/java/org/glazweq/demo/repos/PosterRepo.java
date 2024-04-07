package org.glazweq.demo.repos;

import org.glazweq.demo.domain.Poster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosterRepo extends JpaRepository<Poster, Long> {
    List<Poster> findByGenre(String genre);
}