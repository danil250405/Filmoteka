package org.glazweq.demo.repos;

import org.glazweq.demo.domain.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepo extends CrudRepository<Review, Long> {
   List<Review> findByMovieId(int movieId);
   Review findByMovieIdAndUserId(int movieId, Long user_id);
   void deleteReviewById(Long id);
}
