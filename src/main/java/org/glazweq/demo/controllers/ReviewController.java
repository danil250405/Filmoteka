package org.glazweq.demo.controllers;

import org.glazweq.demo.domain.Review;
import org.glazweq.demo.domain.User;
import org.glazweq.demo.service.FiltersMovieService;
import org.glazweq.demo.service.ReviewService;
import org.glazweq.demo.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReviewController {
    private final ReviewService reviewService;
    private final UserServiceImpl userServiceImpl;
@Autowired
    public ReviewController(@Qualifier("reviewService") ReviewService reviewService, UserServiceImpl userServiceImpl) {
        this.reviewService = reviewService;
    this.userServiceImpl = userServiceImpl;
}



    @PostMapping("/add-review")
    public String addReview(
                            @RequestParam("movieId") int movieId,
                            @RequestParam("reviewText") String reviewText,
                            @RequestParam(value = "reviewScore", required = false) Double reviewScore,
                            Model model
                            ) {
        System.out.println("мы в  AADD REVIEW");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User authUser = userServiceImpl.findUserByEmail(currentPrincipalName);
        Review existingReview = reviewService.findReviewByMovieIdAndUserId(movieId, authUser.getId());
        if (existingReview != null) {
            // Обновить поля существующего отзыва
            existingReview.setReviewText(reviewText);
            existingReview.setReviewScore(reviewScore);

            // Сохранить обновленный отзыв
            reviewService.updateReviewInDB(existingReview);
        } else {
            if (reviewScore > 0 && reviewScore <= 10) {


                if (authUser != null) {
                    reviewService.saveReviewInDB(reviewText, reviewScore, movieId, authUser);
                }
            } else {
                System.out.println("scoreError");
                model.addAttribute("scoreError", "Enter valid score");
            }
            // Сохраняем отзыв в базе данных


        }
        return "redirect:/movieId=" + movieId;
    }
    @PostMapping("/add-new-review")
    public String addNewReview(
            @RequestParam("movieIdNew") int movieId,
            @RequestParam("reviewTextNew") String reviewText,
            @RequestParam(value = "reviewScoreNew", required = false) Double reviewScore,
            Model model
    ) {
        System.out.println("мы в  AADD new REVIEW");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User authUser = userServiceImpl.findUserByEmail(currentPrincipalName);
        Review existingReview = reviewService.findReviewByMovieIdAndUserId(movieId, authUser.getId());
        if (existingReview != null) {
            // Обновить поля существующего отзыва
            existingReview.setReviewText(reviewText);
            existingReview.setReviewScore(reviewScore);

            // Сохранить обновленный отзыв
            reviewService.updateReviewInDB(existingReview);
        } else {
            if (reviewScore > 0 && reviewScore <= 10) {


                if (authUser != null) {
                    reviewService.saveReviewInDB(reviewText, reviewScore, movieId, authUser);
                }
            } else {
                System.out.println("scoreError");
                model.addAttribute("scoreError", "Enter valid score");
            }
            // Сохраняем отзыв в базе данных


        }
        return "redirect:/movieId=" + movieId;
    }
    @PostMapping("/delete-review")
    public String deleteReview(@RequestParam("userReviewId") Long reviewId,
                               @RequestParam("movieId") Long movieId){
    reviewService.deleteReview(reviewId);
    return "redirect:/movieId=" + movieId;
    }
}
