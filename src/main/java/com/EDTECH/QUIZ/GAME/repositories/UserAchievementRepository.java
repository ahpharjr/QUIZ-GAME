package com.EDTECH.QUIZ.GAME.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EDTECH.QUIZ.GAME.models.UserAchievement;
import com.EDTECH.QUIZ.GAME.models.Users;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long>{
    
    Optional<UserAchievement> findByUser(Users user);
    
}
