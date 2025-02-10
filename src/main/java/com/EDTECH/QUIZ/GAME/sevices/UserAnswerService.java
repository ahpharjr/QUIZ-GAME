package com.EDTECH.QUIZ.GAME.sevices;

import org.springframework.stereotype.Service;
import com.EDTECH.QUIZ.GAME.models.UserAnswer;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserAnswerService {

    private final List<UserAnswer> userAnswers = new ArrayList<>();
    private long startTime;
    private Long quizId;  

    public void startQuiz(Long quizId) {
        this.startTime = System.currentTimeMillis();
        this.quizId = quizId;
        userAnswers.clear(); // Clear previous answers
    }

    public void storeAnswer(Long questionId, Long answerId, boolean isCorrect) {
        Long answerTime = System.currentTimeMillis() - startTime;
        userAnswers.add(new UserAnswer(questionId, answerId, isCorrect, answerTime));
    }

    // public int calculatePoints() {
    //     int pointsPerCorrectAnswer = 10;
    //     return (int) userAnswers.stream().filter(UserAnswer::isCorrect).count() * pointsPerCorrectAnswer;
    // }

    public int calculatePoints() {
        int basePointsPerCorrectAnswer = 10; // Default points per correct answer
    
        return userAnswers.stream()
            .filter(UserAnswer::isCorrect) // Consider only correct answers
            .mapToInt(answer -> {
                int basePoints = basePointsPerCorrectAnswer;
                int timeBonus = getTimeBonus(answer.getAnswerTime());
                return basePoints + timeBonus;
            })
            .sum();
    }
    
    // Apply time-based bonuses and penalties
    private int getTimeBonus(Long answerTime) {
        if (answerTime <= 5000) { // Answered within 5 sec
            return 5; // +5 bonus
        } else if (answerTime <= 10000) { // 5 - 10 sec
            return 3; // No bonus, no penalty
        } else if (answerTime <= 20000) { // 10 - 20 sec
            return 1; // Small penalty
        } else { // More than 20 sec
            return 0; // Heavy penalty
        }
    }
    

    public long getTimeTaken() {
        return System.currentTimeMillis() - startTime;
    }

    public Long getQuizId() {
        return quizId;
    }
}

