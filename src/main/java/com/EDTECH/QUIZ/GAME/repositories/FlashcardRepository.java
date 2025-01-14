package com.EDTECH.QUIZ.GAME.repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.EDTECH.QUIZ.GAME.models.Flashcard;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long>{
    List<Flashcard> findAllByTopicTopicId(Long topicId);

    //method for keyword search
    @Query("SELECT f FROM Flashcard f WHERE LOWER(f.keyword) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Flashcard> searchByKeyword(@Param("keyword") String keyword);
}
