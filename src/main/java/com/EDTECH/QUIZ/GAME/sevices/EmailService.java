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
}


// @Service
// public class EmailService {

//     @Autowired
//     private JavaMailSender mailSender;

//     @Value("${app.base-url}")
//     private String baseUrl;

//     public void sendVerificationEmail(String email, String token) {
//         String subject = "Verify Your Email";
//         String verificationUrl = baseUrl + "/verify-email?token=" + token;

//         String message = "Click the link below to verify your email:\n" + verificationUrl;

//         SimpleMailMessage mailMessage = new SimpleMailMessage();
//         mailMessage.setTo(email);
//         mailMessage.setSubject(subject);
//         mailMessage.setText(message);
//         mailSender.send(mailMessage);
//     }
// }

// This is the EmailService class which is taken from the security files.

// @Service
// public class EmailService {
//     @Autowired
//     private JavaMailSender emailSender;

//     public void sendVerificationEmail(String to, String subject, String text) throws MessagingException {
//         MimeMessage message = emailSender.createMimeMessage();
//         MimeMessageHelper helper = new MimeMessageHelper(message, true);

//         helper.setTo(to);
//         helper.setSubject(subject);
//         helper.setText(text, true);

//         emailSender.send(message);
//     }
// }

