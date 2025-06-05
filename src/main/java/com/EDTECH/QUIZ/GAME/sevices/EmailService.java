package com.EDTECH.QUIZ.GAME.sevices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
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

        /**
     * Function Objective: Sends a verification email to the user with a provided token.
     * 
     * Input Parameters :
     *   @param email - the recipient's email address
     *   @param token - the token used to verify the email address
     * 
     * Function Description:
     *   Constructs an email with a verification link and sends it using the configured mail sender.
     *   Logs the result of the operation.
     * 
     * Return Value : void
     * 
     */
    public void sendVerificationEmail(String email, String token) {
        // Validate parameters with error messages
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be null or blank");
        }
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token must not be null or blank");
        }

        try {
            String subject = "Verify Your Email";
            String verificationUrl = baseUrl + "/verify-email?token=" + token;
            String message = "Click the link below to verify your email:\n" + verificationUrl;

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);

            mailSender.send(mailMessage);
            logger.info("Verification email sent successfully to: {}", email);
        } catch (MailException ex) {
            logger.error("MailException while sending verification email to {}: {}", email, ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error("Unexpected error while sending verification email to {}: {}", email, ex.getMessage(), ex);
        }
    }

/**
 * Function Objective: Sends a One-Time Password (OTP) to the user via email.
 *
 * Input Parameters:
 *   @param email - the recipient's email address
 *   @param otp   - the one-time password for account verification or reset
 *
 * Function Description:
 *   Validates the provided email and OTP parameters.
 *   Constructs a simple email message containing the OTP and sends it via the configured mail sender.
 *   Logs the result of the operation and handles both specific mail exceptions and unexpected errors.
 *
 * Return Value: void
 *
 */
public void sendOtpEmail(String email, String otp) {
    // Validate parameters with error messages
    if (email == null || email.isBlank()) {
        throw new IllegalArgumentException("Email must not be null or blank");
    }
    if (otp == null || otp.isBlank()) {
        throw new IllegalArgumentException("OTP must not be null or blank");
    }

    try {
        String subject = "Your OTP Code";
        String message = "Your OTP code is: " + otp + "\nUse this code to reset your password.";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
        logger.info("OTP email sent successfully to: {}", email);
    } catch (MailException ex) {
        logger.error("MailException while sending OTP email to {}: {}", email, ex.getMessage(), ex);
    } catch (Exception ex) {
        logger.error("Unexpected error while sending OTP email to {}: {}", email, ex.getMessage(), ex);
    }
}


    
}

