package com.EDTECH.QUIZ.GAME.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.EDTECH.QUIZ.GAME.models.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    List<Question> findByQuiz_QuizId(Long quizId);

    //@Query(value = "SELECT * FROM questions WHERE quiz_id = :quizId ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    //List<Question> findRandomQuestionsByQuizId(@Param("quizId") Long quizId, @Param("limit") int limit);
    
}
