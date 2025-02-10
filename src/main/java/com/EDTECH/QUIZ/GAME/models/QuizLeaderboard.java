package com.EDTECH.QUIZ.GAME.models;

import jakarta.persistence.*;

@Entity
@Table(name = "quiz_leaderboards")
public class QuizLeaderboard {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizLeaderboardId;

    private int point;
    private Long timeTaken;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    public QuizLeaderboard(){
        
    }

    public QuizLeaderboard(int point, Long timeTaken, Users user, Quiz quiz) {
        this.point = point;
        this.timeTaken = timeTaken;
        this.user = user;
        this.quiz = quiz;
    }

    public Long getQuizLeaderboardId() {
        return quizLeaderboardId;
    }

    public void setQuizLeaderboardId(Long quizLeaderboardId) {
        this.quizLeaderboardId = quizLeaderboardId;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public Long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    
}
