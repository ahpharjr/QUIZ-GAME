package com.EDTECH.QUIZ.GAME.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.EDTECH.QUIZ.GAME.models.Answer;
import com.EDTECH.QUIZ.GAME.models.Question;
import com.EDTECH.QUIZ.GAME.models.Quiz;
import com.EDTECH.QUIZ.GAME.repositories.AnswerRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuestionRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuizRepository;

import jakarta.websocket.server.PathParam;

@Controller
public class QuizController {
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @GetMapping("/{phaseId}/{topicId}/quiz")
    public String quiz(@PathVariable("topicId") Long topicId, Model model) {
        Quiz quiz = quizRepository.findQuizIdByTopic_TopicId(topicId);
        Long quizId = quiz.getQuizId();
        var questions = questionRepository.findByQuiz_QuizId(quizId);
        model.addAttribute("questions", questions); // Pass all questions for context if needed
        model.addAttribute("currentQuestion", questions.get(0)); // Show the first question
        var answers = answerRepository.findByQuestion_QuestionId(questions.get(0).getQuestionId());
        model.addAttribute("answers", answers);
        return "quiz";
    }

    @GetMapping("/quiz/next-question")
    @ResponseBody
    public Map<String, Object> getNextQuestion(@PathParam("questionId") Long questionId) {
        Question currentQuestion = questionRepository.findById(questionId).orElseThrow();
        Quiz quiz = currentQuestion.getQuiz();
        List<Question> questions = questionRepository.findByQuiz_QuizId(quiz.getQuizId());
        int currentIndex = questions.indexOf(currentQuestion);

        Map<String, Object> response = new HashMap<>();
        if (currentIndex + 1 < questions.size()) {
            Question nextQuestion = questions.get(currentIndex + 1);
            List<Answer> answers = answerRepository.findByQuestion_QuestionId(nextQuestion.getQuestionId());
            response.put("question", nextQuestion);
            response.put("answers", answers);
        } else {
            response.put("message", "Quiz completed.");
        }
        return response;
    }
}
