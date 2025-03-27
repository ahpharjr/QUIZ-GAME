package com.EDTECH.QUIZ.GAME.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table (name = "password_reset_token")
public class PasswordResetToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resetTokenId;

    private String token;
    private LocalDateTime expiryDate;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    public PasswordResetToken() {
    }

    public PasswordResetToken(String token, Users user) {
        this.token = token;
        this.user = user;
        this.expiryDate = LocalDateTime.now().plusMinutes(15);//15 minutes expiry  
    }

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public Long getResetTokenId() {
        return resetTokenId;
    }

    public void setResetTokenId(Long resetTokenId) {
        this.resetTokenId = resetTokenId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    

}
