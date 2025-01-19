package com.EDTECH.QUIZ.GAME.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.EDTECH.QUIZ.GAME.repositories.TopicRepository;

@Controller
public class QuizSetController {
    @Autowired
    private TopicRepository topicRepository;
    
    @GetMapping("{phaseId}/quizset")
    public String quizSet(@PathVariable Long phaseId, Model model) {

        model.addAttribute("topics", topicRepository.findAllByPhasePhaseId(phaseId));
        return "quiz_set";
    }
}
