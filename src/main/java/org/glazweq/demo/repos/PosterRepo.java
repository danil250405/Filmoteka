package org.glazweq.demo.repos;

import org.glazweq.demo.domain.Poster;
import org.glazweq.demo.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosterRepo extends CrudRepository<Poster, Long> {
    List<Poster> findByKeyword(String keyword);
}