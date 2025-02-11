package com.EDTECH.QUIZ.GAME.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.EDTECH.QUIZ.GAME.models.Answer;
import com.EDTECH.QUIZ.GAME.models.Question;
import com.EDTECH.QUIZ.GAME.models.UserAnswer;
import com.EDTECH.QUIZ.GAME.repositories.AnswerRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuestionRepository;
import com.EDTECH.QUIZ.GAME.repositories.UserAnswerRepository;

@Controller
public class ReviewController {

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @GetMapping("/{quizAttemptId}/review")
    public String review(Model model, @PathVariable Long quizAttemptId) {
        List<UserAnswer> userAnswers = userAnswerRepository.findByQuizAttempt_QuizAttemptId(quizAttemptId);

        List<Map<String, Object>> reviewData = new ArrayList<>();

        for (UserAnswer userAnswer : userAnswers) {
            Map<String, Object> entry = new HashMap<>();
            Question question = questionRepository.findById(userAnswer.getQuestionId()).orElse(null);
            Answer selectedAnswer = answerRepository.findById(userAnswer.getAnswerId()).orElse(null);
            Answer correctAnswer = answerRepository.findCorrectAnswerByQuestionId(userAnswer.getQuestionId());

            entry.put("questionText", question != null ? question.getQuestion() : "Unknown Question");
            entry.put("selectedAnswer", selectedAnswer != null ? selectedAnswer.getAnswer() : "No Answer Selected");
            entry.put("correctAnswer", correctAnswer != null ? correctAnswer.getAnswer() : "No Correct Answer");
            entry.put("isCorrect", userAnswer.isCorrect());

            reviewData.add(entry);
        }

        model.addAttribute("reviewData", reviewData);
        return "review";
    }
}
