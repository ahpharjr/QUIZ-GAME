package com.EDTECH.QUIZ.GAME.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EDTECH.QUIZ.GAME.models.Quiz;
import com.EDTECH.QUIZ.GAME.models.QuizAttempt;
import com.EDTECH.QUIZ.GAME.models.Users;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long>{
    
    List<QuizAttempt> findByUserAndQuiz(Users user, Quiz quiz);
}
