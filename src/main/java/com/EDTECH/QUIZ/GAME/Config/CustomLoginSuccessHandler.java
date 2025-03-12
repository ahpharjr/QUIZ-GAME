package com.EDTECH.QUIZ.GAME.Config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;
import com.EDTECH.QUIZ.GAME.sevices.JwtService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtService jwtService;  

    @Autowired
    private UserDetailsService userDetailsService; 

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        
        System.out.println("++++++++++++++++++++  Custom Login Success Handler  ==============");
        System.out.println("Username to lead the user in the success handler => " + authentication.getName());
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());

        System.out.println("++++++++++++++++++++  Load By Username for the Register user  ==============");
        System.out.println(userDetails.getUsername());
        String jwtToken = jwtService.generateToken(userDetails); // Correct JWT generation
        System.out.println("++++++++++++++++++++  Token taken from user details  ==============");
        System.out.println("JWT Token: " + jwtToken);
        Cookie jwtCookie = new Cookie("JWT_TOKEN", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60 * 24);
        System.out.println("++++++++++++++++++++  Token Before the cookie in added ==============");
        System.out.println("JWT Token: " + jwtToken);
        response.addCookie(jwtCookie);
                
        SecurityContextHolder.getContext().setAuthentication(authentication);

        response.sendRedirect("/home");
    }
    
}
