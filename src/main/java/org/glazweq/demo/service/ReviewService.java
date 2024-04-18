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


    public void saveReviewInDB(String text, double score, int movieId, User authUser){
//        User user = userRepo.findById(userId);
//                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Review review = new Review(text, score, Timestamp.valueOf(LocalDateTime.now()), movieId, authUser);

        reviewRepo.save(review);
    }
}
