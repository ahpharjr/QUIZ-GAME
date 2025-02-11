package com.EDTECH.QUIZ.GAME.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "user_answers")
public class UserAnswer {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long userAnswerId;

    @Column(name = "question_ref_id") // Rename questionId column in DB
    private Long questionId;

    @Column(name = "answer_ref_id") // Rename answerId column in DB
    private Long answerId;

    private boolean isCorrect;
    private Long answerTime;

    @ManyToOne
    @JoinColumn(name = "quiz_attempt_id", nullable = false)
    private QuizAttempt quizAttempt;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "questionId", insertable = false, updatable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "answer_id", referencedColumnName = "answerId", insertable = false, updatable = false)
    private Answer selectedAnswer;

    public UserAnswer(){
        
    }

    public UserAnswer(Long questionId, Long answerId, boolean isCorrect, Long answerTime) {
        this.questionId = questionId;
        this.answerId = answerId;
        this.isCorrect = isCorrect;
        this.answerTime = answerTime;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer getSelecAnswer() {
        return selectedAnswer;
    }

    public void setSelecAnswer(Answer selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
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

    public Long getUserAnswerId() {
        return userAnswerId;
    }

    public void setUserAnswerId(Long userAnswerId) {
        this.userAnswerId = userAnswerId;
    }

    public QuizAttempt getQuizAttempt() {
        return quizAttempt;
    }

    public void setQuizAttempt(QuizAttempt quizAttempt) {
        this.quizAttempt = quizAttempt;
    }
}

