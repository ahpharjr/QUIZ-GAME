package com.EDTECH.QUIZ.GAME.models;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "quiz_attempts")
public class QuizAttempt {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long quizAttemptId;

    private int totalPoints;
    private long totalTime;
    private int bonusXp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserAnswer> userAnswers;

    public Long getQuizAttemptId() {
        return quizAttemptId;
    }

    public void setQuizAttemptId(Long quizAttemptId) {
        this.quizAttemptId = quizAttemptId;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public Users getUser() {
        return user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<UserAnswer> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(List<UserAnswer> userAnswers) {
        this.userAnswers = userAnswers;
    }
    
    public int getBonusXp() {
        return bonusXp;
    }

    public void setBonusXp(int bonusXp) {
        this.bonusXp = bonusXp;
    }
    
}
