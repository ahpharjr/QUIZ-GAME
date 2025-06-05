package com.EDTECH.QUIZ.GAME.sevices;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EDTECH.QUIZ.GAME.models.Leaderboard;
import com.EDTECH.QUIZ.GAME.models.Phase;
import com.EDTECH.QUIZ.GAME.repositories.LeaderboardRepository;

@Service
public class LeaderboardService {

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private UserAnswerService userAnswerService;
    
    public List<Map<String, Object>> getLeaderboardEntries(Phase phase) {
        List<Leaderboard> leaderboards = leaderboardRepository.findByPhaseOrderByPointDescTimeTakenAsc(phase);

        System.out.println("Number of Leaderboards: " + leaderboards.size());
        System.out.println("===== Raw Leaderboard Data =====");
        for (Leaderboard leaderboard : leaderboards) {
            String username = leaderboard.getUser() != null ? leaderboard.getUser().getUsername() : "null";
            System.out.println("User: " + username +
                    ", Point: " + leaderboard.getPoint() );
                }
        System.out.println("================================");

        List<Map<String, Object>> formattedLeaderboards = new ArrayList<>();

        for (Leaderboard leaderboard : leaderboards) {
            Map<String, Object> entry = new HashMap<>();

            String username = leaderboard.getUser() != null ? leaderboard.getUser().getUsername() : "Unknown";
            String phaseName = leaderboard.getPhase() != null ? leaderboard.getPhase().getName() : "Unknown Quiz";

            entry.put("username", username); 
            entry.put("score", leaderboard.getPoint()); 
            entry.put("phaseName", phaseName);
            formattedLeaderboards.add(entry);
        }

        System.out.println("===== Formatted Leaderboard Entries =====");
        for (Map<String, Object> entry : formattedLeaderboards) {
            System.out.println("Username: " + entry.get("username") +
                    ", Score: " + entry.get("score") +
                    ", Phase Name: " + entry.get("phaseName"));
        }
        System.out.println("=========================================");

        return formattedLeaderboards;
    }


}
