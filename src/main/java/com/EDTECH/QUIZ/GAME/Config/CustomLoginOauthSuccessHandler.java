package com.EDTECH.QUIZ.GAME.Config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.EDTECH.QUIZ.GAME.sevices.JwtService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginOauthSuccessHandler implements AuthenticationSuccessHandler {
    
    @Autowired
    private JwtService jwtService;  

    @Autowired
    private UserDetailsService userDetailsService; 


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        String jwtToken = jwtService.generateToken(userDetails); // Correct JWT generation
        System.out.println("++++++++++++++++++++  Token in the google oauth authentication of userdetails  ==============");
        System.out.println("JWT Token: " + jwtToken);
        Cookie jwtCookie = new Cookie("JWT_TOKEN", jwtToken);
        
        jwtCookie.setSecure(true); // Ensure HTTPS
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60 * 24);
        jwtCookie.setAttribute("SameSite", "Strict"); // Prevent CSRF
        System.out.println("++++++++++++++++++++  Token Before the cookie in added ==============");
        System.out.println("JWT Token: " + jwtToken);
        response.addCookie(jwtCookie);
        response.sendRedirect("/home");    
    }
}
