package com.EDTECH.QUIZ.GAME.models;

import jakarta.persistence.*;

@Entity
@Table(name = "user_achievements")
public class UserAchievement {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long userAchievementId;

    private Boolean achievePhase1;
    private Boolean achievePhase2;
    private Boolean achievePhase3;
    private Boolean achievePhase4;
    private Boolean achieveFirstAchiever;
    private Boolean achieveRisingStar;
    private Boolean achieveRookieQuizzer;
    private Boolean achieveGenius;

    private Boolean displayedPhase1 = false;
    private Boolean displayedPhase2 = false;
    private Boolean displayedPhase3 = false;
    private Boolean displayedPhase4 = false;
    private Boolean displayedFirstAchiever = false;
    private Boolean displayedRisingStar = false;
    private Boolean displayedRookieQuizzer = false;
    private Boolean displayedGenius = false;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    public UserAchievement(){
        
    }

    public Long getUserAchievementId() {
        return userAchievementId;
    }

    public void setUserAchievementId(Long userAchievementId) {
        this.userAchievementId = userAchievementId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Boolean getAchievePhase1() {
        return achievePhase1;
    }

    public void setAchievePhase1(Boolean achievePhase1) {
        this.achievePhase1 = achievePhase1;
    }

    public Boolean getAchievePhase2() {
        return achievePhase2;
    }

    public void setAchievePhase2(Boolean achievePhase2) {
        this.achievePhase2 = achievePhase2;
    }

    public Boolean getAchievePhase3() {
        return achievePhase3;
    }

    public void setAchievePhase3(Boolean achievePhase3) {
        this.achievePhase3 = achievePhase3;
    }

    public Boolean getAchievePhase4() {
        return achievePhase4;
    }

    public void setAchievePhase4(Boolean achievePhase4) {
        this.achievePhase4 = achievePhase4;
    }

    public Boolean getAchieveFirstAchiever() {
        return achieveFirstAchiever;
    }

    public void setAchieveFirstAchiever(Boolean achieveFirstAchiever) {
        this.achieveFirstAchiever = achieveFirstAchiever;
    }

    public Boolean getAchieveRisingStar() {
        return achieveRisingStar;
    }

    public void setAchieveRisingStar(Boolean achieveRisingStar) {
        this.achieveRisingStar = achieveRisingStar;
    }

    public Boolean getAchieveRookieQuizzer() {
        return achieveRookieQuizzer;
    }

    public void setAchieveRookieQuizzer(Boolean achieveRookieQuizzer) {
        this.achieveRookieQuizzer = achieveRookieQuizzer;
    }

    public Boolean getAchieveGenius() {
        return achieveGenius;
    }

    public void setAchieveGenius(Boolean achieveGenius) {
        this.achieveGenius = achieveGenius;
    }

    public Boolean getDisplayedPhase1() {
        return displayedPhase1;
    }

    public void setDisplayedPhase1(Boolean displayedPhase1) {
        this.displayedPhase1 = displayedPhase1;
    }

    public Boolean getDisplayedPhase2() {
        return displayedPhase2;
    }

    public void setDisplayedPhase2(Boolean displayedPhase2) {
        this.displayedPhase2 = displayedPhase2;
    }

    public Boolean getDisplayedPhase3() {
        return displayedPhase3;
    }

    public void setDisplayedPhase3(Boolean displayedPhase3) {
        this.displayedPhase3 = displayedPhase3;
    }

    public Boolean getDisplayedPhase4() {
        return displayedPhase4;
    }

    public void setDisplayedPhase4(Boolean displayedPhase4) {
        this.displayedPhase4 = displayedPhase4;
    }

    public Boolean getDisplayedFirstAchiever() {
        return displayedFirstAchiever;
    }

    public void setDisplayedFirstAchiever(Boolean displayedFirstAchiever) {
        this.displayedFirstAchiever = displayedFirstAchiever;
    }

    public Boolean getDisplayedRisingStar() {
        return displayedRisingStar;
    }

    public void setDisplayedRisingStar(Boolean displayedRisingStar) {
        this.displayedRisingStar = displayedRisingStar;
    }

    public Boolean getDisplayedRookieQuizzer() {
        return displayedRookieQuizzer;
    }

    public void setDisplayedRookieQuizzer(Boolean displayedRookieQuizzer) {
        this.displayedRookieQuizzer = displayedRookieQuizzer;
    }

    public Boolean getDisplayedGenius() {
        return displayedGenius;
    }

    public void setDisplayedGenius(Boolean displayedGenius) {
        this.displayedGenius = displayedGenius;
    }

    
}
