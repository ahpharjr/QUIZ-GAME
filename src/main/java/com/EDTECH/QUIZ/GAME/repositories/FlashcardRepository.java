package com.EDTECH.QUIZ.GAME.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.EDTECH.QUIZ.GAME.models.Flashcard;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
    List<Flashcard> findAllByTopicTopicId(Long topicId);

    @Query("SELECT f FROM Flashcard f WHERE LOWER(f.keyword) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Flashcard> searchFlashcards(@Param("searchTerm") String searchTerm);

    // @Query("SELECT DISTINCT f.keyword FROM Flashcard f WHERE LOWER(f.keyword) LIKE LOWER(CONCAT(:prefix, '%'))")
    // List<String> findKeywordsByPrefix(@Param("prefix") String prefix);

    @Query("SELECT DISTINCT f.keyword FROM Flashcard f " +
       "WHERE LOWER(f.keyword) LIKE LOWER(CONCAT('%', :term, '%')) " +
       "ORDER BY LENGTH(f.keyword), f.keyword ASC")
    List<String> findKeywordsByFlexibleSearch(@Param("term") String term);

}



