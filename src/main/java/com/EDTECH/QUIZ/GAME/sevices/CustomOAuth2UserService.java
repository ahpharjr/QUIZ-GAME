package com.EDTECH.QUIZ.GAME.sevices;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;

import java.util.Date;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        System.out.println("Email: " + email);
        System.out.println("Name: " + name);
        Users user = userRepository.findByEmail(email);
        
        if (user == null) {

            user = new Users();
            user.setEmail(email);
            user.setUsername(name);

            user.setEnabled(true);
            user.setAuthProvider(Users.AuthProvider.GOOGLE);

            user.setHighestScore(0);
            user.setQuizSet(0);
            user.setUserXp(0);
            user.setLevel(1);
            user.setPassword("");
            user.setCreatedDate(new Date());
            userRepository.save(user);
        }
        System.out.println("User: in the Oauth Service :  " + user);
        return new CustomOAuth2User(oAuth2User , userRepository);
    }
    
}
//This is user11@gmail.com