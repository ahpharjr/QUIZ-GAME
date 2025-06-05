package com.EDTECH.QUIZ.GAME.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.EDTECH.QUIZ.GAME.models.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    
    List<Answer> findByQuestion_QuestionId(Long questionId);

    // Delete all answers by question ID
    void deleteAllByQuestion_QuestionId(Long questionId);

    // Find the correct answer for a given question
    @Query("SELECT a FROM Answer a WHERE a.question.questionId = :questionId AND a.isCorrect = true")
    Answer findCorrectAnswerByQuestionId(@Param("questionId") Long questionId);
    
}

