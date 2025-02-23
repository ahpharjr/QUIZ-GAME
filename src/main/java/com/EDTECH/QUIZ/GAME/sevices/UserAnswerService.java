package com.EDTECH.QUIZ.GAME.sevices;

import org.springframework.stereotype.Service;
import com.EDTECH.QUIZ.GAME.models.UserAnswer;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserAnswerService {

    private final List<UserAnswer> userAnswers = new ArrayList<>();
    private long startTime;
    private Long quizId;  

    public void startQuiz(Long quizId, Long sessionStartTime) {
        this.startTime = sessionStartTime; // Use session start time instead of new time
        this.quizId = quizId;
        userAnswers.clear(); // Clear previous answers
    }

    public void storeAnswer(Long questionId, Long answerId, boolean isCorrect) {
        Long answerTime = System.currentTimeMillis() - startTime;
        
        if (answerId == null) {
            answerId = -1L; // Use -1 to indicate no answer
        }
    
        userAnswers.add(new UserAnswer(questionId, answerId, isCorrect, answerTime));
    }
    
    public List<UserAnswer> getUserAnswers(){
        return new ArrayList<>(userAnswers);
    }

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
    
    //Give time based score
    private int getTimeBonus(Long answerTime) {
        if (answerTime <= 5000) return 5;
        else if (answerTime <= 10000) return 3;
        else if (answerTime <= 20000) return 1;
        else return 0;
    }
    
    public long getTimeTaken(HttpSession session) {
        Long sessionStartTime = (Long) session.getAttribute("startTime");
        if (sessionStartTime == null) {
            return 0; // Default to 0 if session time is missing
        }
        return System.currentTimeMillis() - sessionStartTime;
    }

    public Long getQuizId() {
        return quizId;
    }

    public int calculateCorrectAnswerPercentage(){
        if (userAnswers.isEmpty()) {
            return 0;
        }

        long correctCount = userAnswers.stream()
            .filter(UserAnswer::isCorrect)
            .count();

        return (int) Math.round(correctCount * 100) / userAnswers.size();
    }

    /**
     * Convert milliseconds to MM:SS format.
     * @param milliseconds Time in milliseconds.
     * @return Formatted time as MM:SS.
     */
    public String convertToMinutesSeconds(long milliseconds) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Convert milliseconds to HH:MM:SS format.
     * @param milliseconds Time in milliseconds.
     * @return Formatted time as HH:MM:SS.
     */
    public String convertToHoursMinutesSeconds(long milliseconds) {
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}

