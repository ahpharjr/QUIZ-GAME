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
        "profile1.jpg", "profile2.jpg", "profile3.jpg", "profile4.jpg", "profile5.jpg",
        "profile6.jpg", "profile7.jpg", "profile8.jpg", "profile9.jpg", "profile10.jpg",
        "profile11.jpg", "profile12.jpg", "profile13.jpg", "profile14.jpg", "profile15.jpg"
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
