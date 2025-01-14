package com.EDTECH.QUIZ.GAME.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table (name = "flashcards")
public class Flashcard {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long cardId;

    private String keyword;
    private String definition;
    private String image;

    @ManyToOne
    @JoinColumn (name = "topic_id", nullable = false)
    @JsonIgnore
    private Topic topic;

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }


}
