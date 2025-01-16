package com.EDTECH.QUIZ.GAME.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EDTECH.QUIZ.GAME.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
}
