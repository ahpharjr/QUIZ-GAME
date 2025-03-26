package com.EDTECH.QUIZ.GAME.sevices;

import java.util.Arrays;
import java.util.List;

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

    private static final String[] PROFILE_IMAGES = {
        "profile1.png", "profile2.png", "profile3.png", "profile4.png", "profile5.png",
        "profile6.png", "profile7.png", "profile8.png", "profile9.png", "profile10.png",
        "profile11.png", "profile12.png", "profile13.png", "profile14.png", "profile15.png", "profile16.png", "profile17.png","profile18.png", "profile19.png", "profile20.png"
    };

    public String getProfileImage(int userId) {

        int index = (int) (userId % 15);
        
        return PROFILE_IMAGES[index];
    }

    // Method to get the list of profile images
    public List<String> getProfileImages() {
        return Arrays.asList(PROFILE_IMAGES);
    }

}
