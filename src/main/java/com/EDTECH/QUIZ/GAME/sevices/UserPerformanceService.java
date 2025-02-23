package com.EDTECH.QUIZ.GAME.sevices;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EDTECH.QUIZ.GAME.models.Phase;
import com.EDTECH.QUIZ.GAME.models.Quiz;
import com.EDTECH.QUIZ.GAME.models.QuizAttempt;
import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.QuizAttemptRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuizRepository;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;

@Service
public class UserPerformanceService {
    
    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;

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

    public void updateBonusXp(Users user, Quiz quiz){
        List<QuizAttempt> quizAttempts = quizAttemptRepository.findByUserAndQuiz(user, quiz);
        
        if(quizAttempts.isEmpty()){
            return;
        }

        int highestPreviousBonusXp = quizAttempts.stream()
                            .limit(quizAttempts.size() - 1)
                            .mapToInt(QuizAttempt::getBonusXp)
                            .max()
                            .orElse(0); 

                            System.out.println("Highest Previous Bonus Xp:J::::::::::::>>>>>>>> " + highestPreviousBonusXp);

        QuizAttempt latestAttempt = quizAttempts.get(quizAttempts.size() - 1);
        int lastBonusXp = latestAttempt.getBonusXp();
        int currentXp = user.getUserXp();
        System.out.println("Last Bonus Xp:J::::::::::::>>>>>>>> " + lastBonusXp);
        System.out.println("Current Xp:J::::::::::::>>>>>>>> " + currentXp);

        if (quizAttempts.size() > 1 && lastBonusXp > highestPreviousBonusXp){
            int updateBonusXp = currentXp + lastBonusXp - highestPreviousBonusXp;
            System.out.println("Update Bonus Xp:J::::::::::::>>>>>>>> " + updateBonusXp);
            user.setUserXp(updateBonusXp);
            userRepository.save(user);
        }
        
    }

    public void grantXpForPhaseCompletion(Users user, Phase phase) {
        Long phaseId = phase.getPhaseId();
    
        // Check if XP was already granted for this phase
        if (user.hasCompletedPhase(phaseId)) {
            System.out.println("XP already granted for this phase. Skipping.");
            return; // Prevent duplicate XP
        }
    
        // Check if all quizzes in the phase are completed
        List<Quiz> quizzesInPhase = quizRepository.findByTopicPhase(phase);
        List<QuizAttempt> userAttemptsInPhase = quizAttemptRepository.findByUserAndQuizTopicPhase(user, phase);
    
        Set<Long> completedQuizIds = userAttemptsInPhase.stream()
            .map(attempt -> attempt.getQuiz().getQuizId())
            .collect(Collectors.toSet());
    
        boolean allQuizzesCompleted = quizzesInPhase.stream()
            .allMatch(quiz -> completedQuizIds.contains(quiz.getQuizId()));
    
        if (allQuizzesCompleted) {
            user.setUserXp(user.getUserXp() + 1000);
            user.markPhaseCompleted(phaseId); // Mark phase as completed
            userRepository.save(user);
            System.out.println("All quizzes completed in phase! Granting 1000 XP.");
        }
    }
    

    // public void checkAndGrantPhaseCompletionBonus(Users user, Quiz quiz) {
    //     Phase currentPhase = quiz.getTopic().getPhase();
    //     List<Quiz> quizzesInPhase = quizRepository.findByTopicPhase(currentPhase);
    //     List<QuizAttempt> userAttemptsInPhase = quizAttemptRepository.findByUserAndQuizTopicPhase(user, currentPhase);

    //     // Get the distinct quizzes the user has completed
    //     Set<Long> completedQuizIds = userAttemptsInPhase.stream()
    //             .map(attempt -> attempt.getQuiz().getQuizId())
    //             .collect(Collectors.toSet());

    //     // Compare with total quizzes in phase
    //     boolean allQuizzesCompleted = quizzesInPhase.stream()
    //             .allMatch(quizItem -> completedQuizIds.contains(quizItem.getQuizId()));

    //     if (allQuizzesCompleted) {
    //         user.setUserXp(user.getUserXp() + 1000);
    //         System.out.println("All quizzes completed in phase! Granting 1000 XP");
    //         userRepository.save(user);
    //     }
    // }

    public String formatTimeSpent(long timeSpent){
        long totalMinutes = timeSpent / 60000;
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        return String.format("%02dh %02dm", hours, minutes);
    }

}
