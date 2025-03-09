package com.EDTECH.QUIZ.GAME.sevices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendVerificationEmail(String email, String token) {
        try {
            String subject = "Verify Your Email";
            String verificationUrl = baseUrl + "/verify-email?token=" + token;
            String message = "Click the link below to verify your email:\n" + verificationUrl;

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);

            mailSender.send(mailMessage);
            logger.info("Verification email sent successfully to: " + email);
        } catch (Exception e) {
            logger.error("Failed to send verification email to: " + email, e);
        }
    }

    public void sendOtpEmail(String email, String otp) {
        try {
            String subject = "Your OTP Code";
            String message = "Your OTP code is: " + otp + "\nUse this code to reset your password.";

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);

            mailSender.send(mailMessage);
            logger.info("OTP email sent successfully to: " + email);
        } catch (Exception e) {
            logger.error("Failed to send OTP email to: " + email, e);
        }
    }

    
}

