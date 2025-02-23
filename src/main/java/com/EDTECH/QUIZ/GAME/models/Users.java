package com.EDTECH.QUIZ.GAME.models;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class Users {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String username;
    private String email;
    
    @Column(nullable = true)
    private String password;
    private int level;
    private int userXp;
    private long timeSpent;
    private int quizSet;
    private int highestScore;
    private String pfPicture;
    private Date createdDate;
    
    private boolean enabled = false;
    private String verificationToken;
    
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    public enum AuthProvider {
        LOCAL, GOOGLE
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Collection> collections;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuizAttempt> quizAttempts;

    @ElementCollection
    private Set<Long> completedPhases = new HashSet<>();

    public Users() {
    }

    public Users(int userId) {
        this.userId = userId;
    }

    public boolean hasCompletedPhase(Long phaseId) {
        return completedPhases.contains(phaseId);
    }

    public void markPhaseCompleted(Long phaseId) {
        completedPhases.add(phaseId);
    }

    public Set<Long> getCompletedPhases() {
        return completedPhases;
    }

    public void setCompletedPhases(Set<Long> completedPhases) {
        this.completedPhases = completedPhases;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getUserXp() {
        return userXp;
    }

    public void setUserXp(int userXp) {
        this.userXp = userXp;
    }

    

    public int getQuizSet() {
        return quizSet;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public List<QuizAttempt> getQuizAttempts() {
        return quizAttempts;
    }

    public void setQuizAttempts(List<QuizAttempt> quizAttempts) {
        this.quizAttempts = quizAttempts;
    }

    public void setQuizSet(int quizSet) {
        this.quizSet = quizSet;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public String getPfPicture() {
        return pfPicture;
    }

    public void setPfPicture(String pfPicture) {
        this.pfPicture = pfPicture;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

}
