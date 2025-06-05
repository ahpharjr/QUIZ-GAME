package com.EDTECH.QUIZ.GAME.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EDTECH.QUIZ.GAME.models.PasswordResetToken;
import com.EDTECH.QUIZ.GAME.models.Users;


@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUser(Users user);
}
