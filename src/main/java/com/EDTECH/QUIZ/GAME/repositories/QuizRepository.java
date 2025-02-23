package com.EDTECH.QUIZ.GAME.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EDTECH.QUIZ.GAME.models.Phase;
import com.EDTECH.QUIZ.GAME.models.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    
    Quiz findQuizIdByTopic_TopicId(Long topicId);

    List<Quiz> findByTopicPhase(Phase phase);
 
}
