package com.EDTECH.QUIZ.GAME.models;

import jakarta.persistence.*;

@Entity
@Table(name = "collections")
public class Collection {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long collectionId;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Flashcard flashcard;

    public Long getCollectionId() {
        return collectionId;
    }

    public User getUser() {
        return user;
    }

    public Flashcard getFlashcard() {
        return flashcard;
    }

    
}
