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
import org.springframework.web.bind.annotation.ResponseBody;

import com.EDTECH.QUIZ.GAME.models.Leaderboard;
import com.EDTECH.QUIZ.GAME.models.Phase;
import com.EDTECH.QUIZ.GAME.repositories.LeaderboardRepository;
import com.EDTECH.QUIZ.GAME.repositories.PhaseRepository;
import com.EDTECH.QUIZ.GAME.repositories.TopicRepository;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;
import com.EDTECH.QUIZ.GAME.sevices.UserAnswerService;

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

    @Autowired
    private UserAnswerService userAnswerService;

    @GetMapping("{phaseId}/quizset")
    public String quizSet(@PathVariable Long phaseId, Model model, Principal principal) {
        Phase phase = phaseRepository.findById(phaseId).orElse(null);
        model.addAttribute("phase", phase);
        model.addAttribute("topics", topicRepository.findAllByPhasePhaseId(phaseId));

        List<Leaderboard> leaderboards = leaderboardRepository.findByPhaseOrderByPointDescTimeTakenAsc(phase);

        // Create a list to store leaderboard data with formatted time
        
        List<Map<String, Object>> formattedLeaderboards = new ArrayList<>();

        int limit = Math.min(leaderboards.size(), 30);
        
        for (int i = 0; i < limit; i++) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("user", leaderboards.get(i).getUser());
            entry.put("point", leaderboards.get(i).getPoint());
            entry.put("timeTaken", userAnswerService.convertToHoursMinutesSeconds(leaderboards.get(i).getTimeTaken()));
            formattedLeaderboards.add(entry);
        }
        
        
        // for (Leaderboard leaderboard : leaderboards) { 
        //     Map<String, Object> entry = new HashMap<>();
        //     entry.put("user", leaderboard.getUser());
        //     entry.put("point", leaderboard.getPoint());
        //     entry.put("timeTaken", userAnswerService.convertToHoursMinutesSeconds(leaderboard.getTimeTaken()));
        //     formattedLeaderboards.add(entry);
        // }


        model.addAttribute("leaderboards", formattedLeaderboards);

        if (principal != null) {
            var user = userRepository.findByEmail(principal.getName());
            model.addAttribute("user", user);
            Leaderboard userLeaderboard = leaderboardRepository.findByUserAndPhase(user, phase);

            if (userLeaderboard == null) { 
                userLeaderboard = new Leaderboard(0, 0L, user, phase);
                leaderboardRepository.save(userLeaderboard);
            }
            String userTimeTaken = userAnswerService.convertToHoursMinutesSeconds(userLeaderboard.getTimeTaken());

            model.addAttribute("userLeaderboard", userLeaderboard);
            model.addAttribute("userTimeTaken", userTimeTaken);

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

    @GetMapping("/leaderboard/{phaseId}")
    @ResponseBody
    public List<Map<String, Object>> getLeaderboard(@PathVariable Long phaseId) {
        Phase phase = phaseRepository.findById(phaseId).orElse(null);
        if (phase == null) {
            return new ArrayList<>(); // Return an empty list if phase doesn't exist
        }

        List<Leaderboard> leaderboards = leaderboardRepository.findByPhaseOrderByPointDescTimeTakenAsc(phase);
        List<Map<String, Object>> formattedLeaderboards = new ArrayList<>();

        for (Leaderboard leaderboard : leaderboards) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("username", leaderboard.getUser().getUsername());
            entry.put("profilePic", "/images/profiles/" + leaderboard.getUser().getPfPicture());
            entry.put("point", leaderboard.getPoint());
            entry.put("timeTaken", userAnswerService.convertToHoursMinutesSeconds(leaderboard.getTimeTaken()));
            formattedLeaderboards.add(entry);
        }

        return formattedLeaderboards;
    }

}
