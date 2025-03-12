package com.EDTECH.QUIZ.GAME.Config;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        String errorMessage = "Invalid username or password";

        if (exception instanceof BadCredentialsException) {
            errorMessage = "Incorrect username or password.";
        } else if (exception instanceof DisabledException) {
            errorMessage = "Your account is disabled. Contact support.";
        } else if (exception instanceof LockedException) {
            errorMessage = "Your account is locked. Try again later.";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "No user found with this username.";
        }

        response.sendRedirect("/login?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8) + "&submitted=true");
    }
}

