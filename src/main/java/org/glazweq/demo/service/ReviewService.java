package org.glazweq.demo.service;

import org.glazweq.demo.domain.Review;
import org.glazweq.demo.domain.User;
import org.glazweq.demo.repos.ReviewRepo;
import org.glazweq.demo.repos.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {
   private UserServiceImpl userServiceImpl;
    private final UserRepo userRepo;
    private ReviewRepo reviewRepo;
    public ReviewService(UserServiceImpl userServiceImpl, UserRepo userRepo, ReviewRepo reviewRepo) {
        this.userServiceImpl = userServiceImpl;
        this.userRepo = userRepo;
        this.reviewRepo = reviewRepo;
    }

    public double getAverageSVRating(List<Review> svReviews){
        int count = 0;
        double totalReviews = 0;
        for (Review svReview : svReviews){
            double svScore = svReview.getReviewScore();
            totalReviews += svScore;
            count++;
        }
        if (count != 0){
            double averageSVRating = totalReviews/count;
            averageSVRating = Math.round(averageSVRating * 10.0) / 10.0;
            return averageSVRating;
        }
        return -1;
    }
    public List<Review> getReviewsByMovieId(int movieId){
        return reviewRepo.findByMovieId(movieId);
    }
    public void saveReviewInDB(String text, double score, int movieId, User authUser){
//        User user = userRepo.findById(userId);
//                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Review review = new Review(text, score, Timestamp.valueOf(LocalDateTime.now()), movieId, authUser);

        reviewRepo.save(review);
    }
}
