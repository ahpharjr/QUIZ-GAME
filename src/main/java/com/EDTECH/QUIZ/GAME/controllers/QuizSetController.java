package com.EDTECH.QUIZ.GAME.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.EDTECH.QUIZ.GAME.models.Leaderboard;
import com.EDTECH.QUIZ.GAME.models.Phase;
import com.EDTECH.QUIZ.GAME.repositories.LeaderboardRepository;
import com.EDTECH.QUIZ.GAME.repositories.PhaseRepository;
import com.EDTECH.QUIZ.GAME.repositories.TopicRepository;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;

@Controller
public class QuizSetController {
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired 
    private UserRepository userRepository;   

    @GetMapping("{phaseId}/quizset")
        public String quizSet(@PathVariable Long phaseId, Model model, Principal principal) {
            Phase phase = phaseRepository.findById(phaseId).orElse(null);
            model.addAttribute("phase", phase);
            model.addAttribute("topics", topicRepository.findAllByPhasePhaseId(phaseId));

            List<Leaderboard> leaderboards = leaderboardRepository.findByPhaseOrderByPointDescTimeTakenAsc(phase);
            model.addAttribute("leaderboards", leaderboards);

            if (principal != null) {
                var user = userRepository.findByUsername(principal.getName());
                Leaderboard userLeaderboard = leaderboardRepository.findByUserAndPhase(user, phase);

                if(userLeaderboard == null){
                    userLeaderboard = new Leaderboard(0, 0L, user, phase);
                }
                model.addAttribute("userLeaderboard", userLeaderboard);

                // Find the user's rank
                int userRank = -1;
                for (int i = 0; i < leaderboards.size(); i++) {
                    if (leaderboards.get(i).getUser().getUserId() == user.getUserId()) {
                        userRank = i + 1; // Rank starts from 1
                        break;
                    }
                }
                model.addAttribute("userRank", userRank);
            }

            return "quiz_set";
        }

}
