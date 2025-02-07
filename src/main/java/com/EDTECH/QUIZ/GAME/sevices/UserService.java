package com.EDTECH.QUIZ.GAME.sevices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenService verificationTokenService;

    public void registerUser(Users user) {
        user.setEnabled(false); 
        userRepository.save(user);
        verificationTokenService.sendVerificationEmail(user);
    }
}
