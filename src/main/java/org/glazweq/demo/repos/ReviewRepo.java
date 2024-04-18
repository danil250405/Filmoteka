package org.glazweq.demo.repos;

import org.glazweq.demo.domain.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepo extends CrudRepository<Review, Long> {
}
