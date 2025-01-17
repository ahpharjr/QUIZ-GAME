package com.EDTECH.QUIZ.GAME.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuizSetController {
    
    @GetMapping("/quizset")
    public String quizSet() {
        return "quiz_set";
    }
}
