package com.EDTECH.QUIZ.GAME.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "collections")
public class Collection {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long collectionId;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private Users user;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    @JsonIgnore
    private Flashcard flashcard;

    public Long getCollectionId() {
        return collectionId;
    }

    public Users getUser() {
        return user;
    }

    public Flashcard getFlashcard() {
        return flashcard;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setFlashcard(Flashcard flashcard) {
        this.flashcard = flashcard;
    }

    
}
