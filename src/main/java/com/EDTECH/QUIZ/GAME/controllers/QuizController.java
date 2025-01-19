package com.EDTECH.QUIZ.GAME.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.EDTECH.QUIZ.GAME.repositories.QuestionRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuizRepository;

import jakarta.websocket.server.PathParam;

@Controller
public class QuizController {
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/{phaseId}/{topicId}/quiz")
    public String quiz(@PathParam("topicId") Long topicId, Model model) {
        Long quizId = quizRepository.findQuizIdByTopic_TopicId(topicId);
        var questions = questionRepository.findByQuiz_QuizId(quizId);
        model.addAttribute("questions", questions);
        
        return "quiz";
    }
}

