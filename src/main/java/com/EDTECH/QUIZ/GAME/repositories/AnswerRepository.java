package com.EDTECH.QUIZ.GAME.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EDTECH.QUIZ.GAME.models.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    
    List<Answer> findByQuestion_QuestionId(Long questionId);
    
}
