package com.EDTECH.QUIZ.GAME.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EDTECH.QUIZ.GAME.models.Quiz;
import com.EDTECH.QUIZ.GAME.models.QuizLeaderboard;
import com.EDTECH.QUIZ.GAME.models.Users;

@Repository
public interface QuizLeaderboardRepository extends JpaRepository<QuizLeaderboard, Long>{
    
    QuizLeaderboard findByUserAndQuiz(Users user,Quiz quiz);
}
