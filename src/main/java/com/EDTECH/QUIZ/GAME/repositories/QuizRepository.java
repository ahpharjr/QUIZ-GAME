package com.EDTECH.QUIZ.GAME.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EDTECH.QUIZ.GAME.models.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    
    Long findQuizIdByTopic_TopicId(Long topicId);
 
}
