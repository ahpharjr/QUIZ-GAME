package com.EDTECH.QUIZ.GAME.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.EDTECH.QUIZ.GAME.models.Leaderboard;
import com.EDTECH.QUIZ.GAME.models.Quiz;
import com.EDTECH.QUIZ.GAME.models.QuizAttempt;
import com.EDTECH.QUIZ.GAME.models.QuizLeaderboard;
import com.EDTECH.QUIZ.GAME.models.UserAnswer;
import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.LeaderboardRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuizAttemptRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuizLeaderboardRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuizRepository;
import com.EDTECH.QUIZ.GAME.repositories.UserAnswerRepository;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;
import com.EDTECH.QUIZ.GAME.sevices.UserAnswerService;

@Controller
public class ResultController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizLeaderboardRepository quizLeaderboardRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private UserAnswerService userAnswerService;
            
    @GetMapping("/{quizId}/result")
    public String showResult(@PathVariable Long quizId,Model model, Principal principal) {

        Users user = userRepository.findByUsername(principal.getName());
        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        List<QuizAttempt> quizAttempts = quizAttemptRepository.findByUserAndQuiz(user, quiz);
        QuizAttempt lastAttempt = quizAttempts.get(quizAttempts.size()-1);

        String lastAttemptTime = userAnswerService.convertToMinutesSeconds(lastAttempt.getTotalTime());

        List<UserAnswer> userAnswers = userAnswerRepository.findByQuizAttempt_QuizAttemptId(lastAttempt.getQuizAttemptId());

        int totalQuestions = userAnswers.size();
        long correctAnswers = userAnswers.stream().filter(UserAnswer::isCorrect).count();
        long incorrectAnswers = totalQuestions - correctAnswers;
        long phaseId = quiz.getTopic().getPhase().getPhaseId();

        model.addAttribute("phaseId", phaseId);
        model.addAttribute("totalQuestions", totalQuestions );
        model.addAttribute("correctAnswers", correctAnswers);
        model.addAttribute("incorrectAnswers", incorrectAnswers);
        model.addAttribute("quizAttempt", lastAttempt);
        model.addAttribute("lastAttemptTime", lastAttemptTime);
        
        List<QuizLeaderboard> quizLeaderboards = quizLeaderboardRepository.findByQuizOrderByPointDescTimeTakenAsc(quiz);
        QuizLeaderboard userLeaderboard = quizLeaderboardRepository.findByUserAndQuiz(user, quiz);

        // Create a list to store leaderboard data with formatted time
        List<Map<String, Object>> formattedLeaderboards = new ArrayList<>();

        for (QuizLeaderboard leaderboard : quizLeaderboards) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("user", leaderboard.getUser());
            entry.put("point", leaderboard.getPoint());
            entry.put("timeTaken", userAnswerService.convertToMinutesSeconds(leaderboard.getTimeTaken()));
            formattedLeaderboards.add(entry);
        }

        model.addAttribute("leaderboards", formattedLeaderboards);
        model.addAttribute("userLeaderboard", userLeaderboard);

        int userRank = -1;
                for (int i = 0; i < quizLeaderboards.size(); i++) {
                    if (quizLeaderboards.get(i).getUser().getUserId() == user.getUserId()) {
                        userRank = i + 1; // Rank starts from 1
                        break;
                    }
                }
        model.addAttribute("userRank", userRank);

        // Update points and time taken for phase leaderboard
        // Fetch user's phase leaderboard
        Leaderboard userPhaseLeaderboard = leaderboardRepository.findByUserAndPhase(user, quiz.getTopic().getPhase());

        if (userPhaseLeaderboard == null) { 
            userPhaseLeaderboard = new Leaderboard(0, 0L, user, quiz.getTopic().getPhase());
        }

        int currentPoints = userPhaseLeaderboard.getPoint();
        long currentTimeTaken = userPhaseLeaderboard.getTimeTaken();

        int lastAttemptPoints = lastAttempt.getTotalPoints();
        long lastAttemptTimeTaken = lastAttempt.getTotalTime();

        // Find the highest points from all previous attempts (excluding the last one)
        int highestPreviousPoints = quizAttempts.stream()
                .limit(quizAttempts.size() - 1)  // Exclude the last attempt
                .mapToInt(QuizAttempt::getTotalPoints)
                .max()
                .orElse(0); // If no previous attempts, default to 0

        // Update only if the last attempt has the most points
        if (lastAttemptPoints > highestPreviousPoints) {
            int updatedPoints = currentPoints - highestPreviousPoints + lastAttemptPoints;
            long updatedTimeTaken = currentTimeTaken - 
                    quizAttempts.stream()
                        .filter(attempt -> attempt.getTotalPoints() == highestPreviousPoints)
                        .mapToLong(QuizAttempt::getTotalTime)
                        .findFirst()
                        .orElse(0L) 
                    + lastAttemptTimeTaken;

            userPhaseLeaderboard.setPoint(updatedPoints);
            userPhaseLeaderboard.setTimeTaken(updatedTimeTaken);
            leaderboardRepository.save(userPhaseLeaderboard); // Save only if updated
        } else if (quizAttempts.size() == 1) {
            // If it's the first attempt, always update
            userPhaseLeaderboard.setPoint(currentPoints + lastAttemptPoints);
            userPhaseLeaderboard.setTimeTaken(currentTimeTaken + lastAttemptTimeTaken);
            leaderboardRepository.save(userPhaseLeaderboard);
        }

        return "result";
    }
}