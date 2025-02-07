package com.EDTECH.QUIZ.GAME.models;

import jakarta.persistence.*;

@Entity
@Table(name = "leaderboards")
public class Leaderboard {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaderboardId;

    private int point;
    private Long timeTaken;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
    
    @ManyToOne
    @JoinColumn(name = "phase_id", nullable = false)
    private Phase phase; 

    public Leaderboard() {
    }

    public Leaderboard(int point, Long timeTaken, Users user, Phase phase) {
        this.point = point;
        this.timeTaken = timeTaken;
        this.user = user;
        this.phase = phase;
    }

    public Long getLeaderboardId() {
        return leaderboardId;
    }

    public void setLeaderboardId(Long leaderboardId) {
        this.leaderboardId = leaderboardId;
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

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }


}
