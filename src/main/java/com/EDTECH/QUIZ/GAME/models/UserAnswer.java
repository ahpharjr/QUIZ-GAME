package com.EDTECH.QUIZ.GAME.models;

import jakarta.persistence.Entity;

// @Entity
public class UserAnswer {
    private Long questionId;
    private Long answerId;
    private boolean isCorrect;
    private Long answerTime;

    public UserAnswer(){
        
    }

    public UserAnswer(Long questionId, Long answerId, boolean isCorrect, Long answerTime) {
        this.questionId = questionId;
        this.answerId = answerId;
        this.isCorrect = isCorrect;
        this.answerTime = answerTime;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Long getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Long answerTime) {
        this.answerTime = answerTime;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}

