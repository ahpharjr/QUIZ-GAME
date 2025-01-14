package com.EDTECH.QUIZ.GAME.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EDTECH.QUIZ.GAME.models.Phase;

@Repository
public interface PhaseRepository extends JpaRepository<Phase, Long>{

    
} 