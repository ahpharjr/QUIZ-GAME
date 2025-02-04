package com.EDTECH.QUIZ.GAME.Config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

// @Component
// public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

//     @Autowired
//     private UserRepository userRepository;

//     @Override
//     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
//             throws IOException, ServletException {
//         String username = authentication.getName();
//         // String email = authentication.getEmail();
//         Users user = userRepository.findByUsername(username);

//         if (user != null) {

//             response.sendRedirect("/home");
//         } else {
//             response.sendRedirect("/login?error");
//         }
//     }


// }
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtService jwtService;  // Use JwtService

    @Autowired
    private UserDetailsService userDetailsService; 

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
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
        response.sendRedirect("/home");
    }
}
