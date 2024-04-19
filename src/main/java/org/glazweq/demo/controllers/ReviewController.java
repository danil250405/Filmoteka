package org.glazweq.demo.controllers;

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
    public String addReview(@RequestParam("movieId") int movieId,
                            @RequestParam("reviewText") String reviewText,
                            @RequestParam(value = "reviewScore", required = false) Double reviewScore,
                            Model model
                            ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String roleAuthUser;
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            roleAuthUser = "admin";
        } else {
            roleAuthUser = "guest";
        }

       if (reviewScore > 0 && reviewScore <=10){
           String userMail = auth.getName();
           User authUser = userServiceImpl.findUserByEmail(userMail);
           if (authUser != null) {
               reviewService.saveReviewInDB(reviewText, reviewScore, movieId, authUser);
           }
       }
       else {
           System.out.println("scoreError");
           model.addAttribute("scoreError", "Enter valid score");
       }
        // Сохраняем отзыв в базе данных
        return "redirect:/movieId=" + movieId;

    }
}
