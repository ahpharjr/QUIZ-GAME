package com.EDTECH.QUIZ.GAME.sevices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EDTECH.QUIZ.GAME.models.QuizAttempt;
import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.QuizAttemptRepository;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;

@Service
public class UserPerformanceService {
    
    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private UserRepository userRepository;

    public void updateHighestScore(Users user){
        List<QuizAttempt> quizAttempts = quizAttemptRepository.findByUser(user);
        
        if(quizAttempts.isEmpty()){
            return;
        }

        //Get the hightest score from all attempts
        int highestScore = quizAttempts.stream()
                            .mapToInt(QuizAttempt::getTotalPoints)
                            .max().orElse(0);

        //Update the user's highest score 
        if (highestScore > user.getHighestScore()){
            user.setHighestScore(highestScore);
            userRepository.save(user);
        }
    }

    public void updateTimeSpent(Users user){
        List<QuizAttempt> quizAttempts = quizAttemptRepository.findByUser(user);
        
        if(quizAttempts.isEmpty()){
            return;
        }

        //Get the total time spent from all attempts
        Long totalTimeSpent = quizAttempts.stream()
                            .mapToLong(QuizAttempt::getTotalTime)
                            .sum();

        user.setTimeSpent(totalTimeSpent);
        System.out.println("Total time spent:J::::::::::::>>>>>>>> " + totalTimeSpent);
        userRepository.save(user);
    }

    public String formatTimeSpent(long timeSpent){
        long totalMinutes = timeSpent / 60000;
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        return String.format("%02dh %02dm", hours, minutes);
    }
}
