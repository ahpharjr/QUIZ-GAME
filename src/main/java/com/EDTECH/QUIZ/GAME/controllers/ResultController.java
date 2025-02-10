package com.EDTECH.QUIZ.GAME.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//  

import com.EDTECH.QUIZ.GAME.models.Quiz;
import com.EDTECH.QUIZ.GAME.models.QuizLeaderboard;
import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.QuizLeaderboardRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuizRepository;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;

@Controller
public class ResultController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizLeaderboardRepository quizLeaderboardRepository;

    @Autowired
    private QuizRepository quizRepository;
            
    @GetMapping("/{quizId}/result")
    public String showResult(@PathVariable Long quizId,Model model, Principal principal) {
        Users user = userRepository.findByUsername(principal.getName());
        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        QuizLeaderboard leaderboard = quizLeaderboardRepository.findByUserAndQuiz(user, quiz);
        System.out.println("in result::::::::;"+ leaderboard);
        //model.addAttribute("leaderboard", leaderboard);

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