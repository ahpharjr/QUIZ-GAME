package com.EDTECH.QUIZ.GAME.sevices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;

import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private UserRepository userRepository;

    public String generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        Users user = userRepository.findByEmail(email);
        user.setOtp(otp);
        userRepository.save(user);
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        Users user = userRepository.findByEmail(email);
        return user.getOtp().equals(otp);
    }

    public void sendOtpToEmail(String email, String otp) {
        System.out.println("Sending OTP " + otp + " to " + email);
    }
}
