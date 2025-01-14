package com.EDTECH.QUIZ.GAME.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EDTECH.QUIZ.GAME.models.Flashcard;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long>{
    List<Flashcard> findAllByTopicTopicId(Long topicId);
}
