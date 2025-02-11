package com.EDTECH.QUIZ.GAME.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EDTECH.QUIZ.GAME.models.UserAnswer;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    
    List<UserAnswer> findByQuizAttempt_QuizAttemptId(long quizAttemptId);
}
