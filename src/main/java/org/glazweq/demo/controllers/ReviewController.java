package org.glazweq.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReviewController {

    @PostMapping("/add-review")
    @ResponseBody
    public String addReview(@RequestParam("reviewText") String reviewText,
                            @RequestParam("reviewScore") double reviewScore) {
        // Проверяем, был ли введен текст отзыва
        if (reviewText.trim().isEmpty()) {
            // Если текст отзыва пустой, возвращаем сообщение об ошибке
            return "Please enter your review.";
        } else {
            // Иначе сохраняем отзыв в базе данных (или выполняем другие действия)
            // Здесь должен быть код сохранения отзыва в базе данных
            return "Review added successfully!";
        }
    }
}
