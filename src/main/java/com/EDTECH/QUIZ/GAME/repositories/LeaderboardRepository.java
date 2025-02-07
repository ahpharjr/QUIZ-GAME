package com.EDTECH.QUIZ.GAME.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.EDTECH.QUIZ.GAME.models.Leaderboard;
import com.EDTECH.QUIZ.GAME.models.Phase;
import com.EDTECH.QUIZ.GAME.models.Users;

public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {

    List<Leaderboard> findByPhaseOrderByPointDescTimeTakenAsc(Phase phase);
    Leaderboard findByUserAndPhase(Users user, Phase phase);
}

