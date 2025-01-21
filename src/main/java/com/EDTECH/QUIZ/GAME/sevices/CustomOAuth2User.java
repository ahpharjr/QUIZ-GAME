package com.EDTECH.QUIZ.GAME.sevices;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;

public class CustomOAuth2User implements OAuth2User {

    
    private final OAuth2User oAuth2User;
    private final UserRepository userRepo;
    
    public CustomOAuth2User(OAuth2User oAuth2User, UserRepository userRepo) {
        this.oAuth2User = oAuth2User;
        this.userRepo = userRepo;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    public int getId() {
        int id = 1;
        System.out.println("OAuth2 User Attributes: " + oAuth2User.getAttributes());
        String email = oAuth2User.getAttribute("email");    
        Users user = userRepo.findByEmail(email);
        id = user.getUserId();
        return id;
    }

    public String getEmail() {
        return oAuth2User.getAttribute("email");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return oAuth2User.getAuthorities();
    }

}
