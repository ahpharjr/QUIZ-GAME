package com.EDTECH.QUIZ.GAME.sevices;

import org.springframework.stereotype.Service;

import com.EDTECH.QUIZ.GAME.models.UserAnswer;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAnswerService {

    private final List<UserAnswer> userAnswers = new ArrayList<>();
    private long startTime;

    public UserAnswerService() {
        this.startTime = System.currentTimeMillis(); // Record quiz start time
    }

    // Store user's answer
    public void storeAnswer(Long questionId, Long answerId, boolean isCorrect) {
        userAnswers.add(new UserAnswer(questionId, answerId, isCorrect));
    }

    // Get all stored answers
    public List<UserAnswer> getUserAnswers() {
        return userAnswers;
    }

    // Calculate total time taken for the quiz
    public String getTimeTaken() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        long minutes = elapsedTime / 60000;
        long seconds = (elapsedTime % 60000) / 1000;

        return String.format("%02d:%02d", minutes, seconds);
    }

    // Reset the service (optional, for new quizzes)
    public void reset() {
        userAnswers.clear();
        startTime = System.currentTimeMillis();
    }
}

