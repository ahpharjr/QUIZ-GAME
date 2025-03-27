package com.EDTECH.QUIZ.GAME.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.EDTECH.QUIZ.GAME.models.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Users findByUsername(String username);

    Users findByEmail(String email);

    Users findByVerificationToken(String token);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Users u WHERE u.enabled = false AND u.createdDate < :cutoffTime")
    void deleteUnverifiedUsersBefore(@Param("cutoffTime") Date cutoffTime);
}
