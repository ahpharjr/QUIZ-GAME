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

import com.EDTECH.QUIZ.GAME.models.Quiz;
import com.EDTECH.QUIZ.GAME.models.QuizAttempt;
import com.EDTECH.QUIZ.GAME.models.QuizLeaderboard;
import com.EDTECH.QUIZ.GAME.models.UserAnswer;
import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.QuizAttemptRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuizLeaderboardRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuizRepository;
import com.EDTECH.QUIZ.GAME.repositories.UserAnswerRepository;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;
import com.EDTECH.QUIZ.GAME.sevices.UserAnswerService;

import jakarta.servlet.http.HttpSession;

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
    private UserAnswerService userAnswerService;
            
    @GetMapping("/{quizId}/result")
    public String showResult(@PathVariable Long quizId,Model model, Principal principal, HttpSession session) {

        Users user = userRepository.findByEmail(principal.getName());
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

        // Clear quiz session data
        if (session.getAttribute("selectedQuestions") != null ||
            session.getAttribute("currentQuestionIndex") != null ||
            session.getAttribute("userAnsers") != null ||
            session.getAttribute("startTime") != null){

                
            session.removeAttribute("selectedQuestions");
            session.removeAttribute("currentQuestionIndex");
            session.removeAttribute("userAnswers");
            session.removeAttribute("startTime");

        }

        return "result";
    }
}