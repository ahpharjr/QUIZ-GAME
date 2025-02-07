package com.EDTECH.QUIZ.GAME.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResultController {
            
    @GetMapping("/result")
    public String result() {
        return "result";
    }
}
// @GetMapping("/result")
// public String getResult(Model model) {
//     // Fetch stored user answers
//     List<UserAnswer> userAnswers = userAnswerService.getUserAnswers();

//     int totalQuestions = userAnswers.size();
//     long correctAnswers = userAnswers.stream().filter(UserAnswer::isCorrect).count();
//     long incorrectAnswers = totalQuestions - correctAnswers;
//     int score = (int) correctAnswers * 10; // Example: 10 points per correct answer

//     model.addAttribute("totalQuestions", totalQuestions);
//     model.addAttribute("correctAnswers", correctAnswers);
//     model.addAttribute("incorrectAnswers", incorrectAnswers);
//     model.addAttribute("score", score);
//     model.addAttribute("timeTaken", userAnswerService.getTimeTaken());

//     return "result";
// }