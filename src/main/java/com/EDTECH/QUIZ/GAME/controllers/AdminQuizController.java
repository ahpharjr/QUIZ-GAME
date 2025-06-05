package com.EDTECH.QUIZ.GAME.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EDTECH.QUIZ.GAME.models.Quiz;
import com.EDTECH.QUIZ.GAME.repositories.PhaseRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuizRepository;
import com.EDTECH.QUIZ.GAME.sevices.QuizService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/admin/quiz")
public class AdminQuizController {
    
    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizRepository quizRepository;
    
    @Autowired
    private PhaseRepository phaseRepository;

    @GetMapping("/phase")
    public String quizInPhase(@RequestParam int phaseId) {
        List<Quiz> quizzes = quizService.getAllQuizByPhaseId(phaseId);
        if (quizzes != null && !quizzes.isEmpty()) {
            return quizzes.toString();
        } else {
            return "No quizzes found for this phase.";
        }
        
    }
    


}
