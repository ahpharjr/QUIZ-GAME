package com.EDTECH.QUIZ.GAME.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.EDTECH.QUIZ.GAME.models.Leaderboard;
import com.EDTECH.QUIZ.GAME.models.Phase;
import com.EDTECH.QUIZ.GAME.repositories.PhaseRepository;
import com.EDTECH.QUIZ.GAME.sevices.LeaderboardService;

import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@RestController
public class CsvController {
    
    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private PhaseRepository phaseRepository;

    @GetMapping("/admin/download-leaderboard/{phaseId}")
    public void downloadLeaderboardCsv(HttpServletResponse response , @PathVariable long phaseId) throws Exception {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"leaderboard.csv\"");

        Phase phase = phaseRepository.findById(phaseId).orElse(null);
        if (phase == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Phase not found");
            return;
        }
        
        List<Map<String, Object>> entries = leaderboardService.getLeaderboardEntries(phase);

        try (PrintWriter writer = response.getWriter()) {

            writer.println("Username,Score,Phase Name");

            for (Map<String, Object> entry : entries) {
                String username = entry.get("username") != null ? entry.get("username").toString() : "";
                int score = entry.get("score") != null ? Integer.parseInt(entry.get("score").toString()) : 0;
                String phaseName = entry.get("phaseName") != null ? entry.get("phaseName").toString() : "";
                writer.printf("%s,%d,%s%n", username, score, phaseName);
            }
        }
    }


}
