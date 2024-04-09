package org.glazweq.demo.repos;

import org.glazweq.demo.domain.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepo extends CrudRepository<Genre, Long> {
    Genre findByName(String name);
}
