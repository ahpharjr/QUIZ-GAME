package com.EDTECH.QUIZ.GAME.sevices;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;

@Service
public class VerificationTokenService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.base-url}") // Store this in application.properties
    private String baseUrl;

    public void sendVerificationEmail(Users user) {

        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        userRepository.save(user); 

        
        String verificationUrl = baseUrl + "/verify-email?token=" + token;

        sendEmail(user.getEmail(), verificationUrl);
    }

    private void sendEmail(String to, String url) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Email Verification");
        message.setText("Click the link to verify your email: " + url);
        mailSender.send(message);
    }
}
