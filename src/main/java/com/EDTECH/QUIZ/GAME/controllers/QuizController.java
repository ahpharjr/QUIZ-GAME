package com.EDTECH.QUIZ.GAME.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuizController {
    
    @GetMapping("/quiz")
    public String quiz() {
        return "quiz";
    }
}
