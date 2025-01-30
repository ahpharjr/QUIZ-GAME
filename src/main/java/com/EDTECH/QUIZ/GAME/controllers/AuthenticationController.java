package com.EDTECH.QUIZ.GAME.controllers;

import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;
import com.EDTECH.QUIZ.GAME.sevices.CustomOAuth2User;


@Controller
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new Users());
        System.out.println("This is the get mapping of register");
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") Users user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setEmail(user.getEmail());
        user.setUsername(user.getUsername());
        user.setHighestScore(0);
        user.setQuizSet(0);
        user.setUserXp(0);
        user.setLevel(1);
        user.setCreatedDate(new Date());
        System.out.println("User: " + user);
        userRepository.save(user);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(){

        return "login";
    }

    @GetMapping("/home")
        public String home(Model model) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null) {
                Object principal = authentication.getPrincipal();

                if (principal instanceof CustomOAuth2User) {
                    CustomOAuth2User customUser = (CustomOAuth2User) principal;
                    Users currentUser = userRepository.findByUsername(customUser.getName());
                    model.addAttribute("user", currentUser);

                } else if (principal instanceof UserDetails) {
                    UserDetails userDetails = (UserDetails) principal;
                    System.out.println(userDetails.getUsername());
                    Users currentUser = userRepository.findByUsername(userDetails.getUsername());

                    model.addAttribute("user", currentUser);
                }
            }

            return "home";
        }

        @GetMapping("/profile")
        public String profile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
            Users currentUser = userRepository.findByUsername(userDetails.getUsername());
            
            if (currentUser == null) {
                return "redirect:/login";  
            }
        
            model.addAttribute("user", currentUser);
            return "update_profile";  
        }
        

    @PostMapping("/edit")
    public String editProfile(@ModelAttribute("user") Users user, Principal principal, Model model) {

        String username = principal.getName();
        Users existingUser = userRepository.findByUsername(username);

        existingUser.setUsername(user.getUsername());
        // existingUser.setEmail(user.getEmail());

        userRepository.save(existingUser);
        model.addAttribute("user", existingUser);  

        
        return "redirect:/home"; 
    }


}