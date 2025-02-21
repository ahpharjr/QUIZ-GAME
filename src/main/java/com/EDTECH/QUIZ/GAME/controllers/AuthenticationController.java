package com.EDTECH.QUIZ.GAME.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.Cookie;

import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;
import com.EDTECH.QUIZ.GAME.sevices.CustomOAuth2User;
import com.EDTECH.QUIZ.GAME.sevices.EmailService;

import jakarta.servlet.http.HttpServletResponse;


@Controller
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String landing() {
        return "landing";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new Users());
        System.out.println("This is the get mapping of register");
        return "register";
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") Users user, Model model) {

        if(userRepository.findByUsername(user.getUsername()) != null) {
            System.out.println("This is the post mapping of register Due to Username");
            model.addAttribute("error", "Username is already taken. Please choose another.");
            return "register";
        }

        if(userRepository.findByEmail(user.getEmail()) != null) {
            System.out.println("This is the post mapping of register Due to Email");
            model.addAttribute("error", "Your email is already registered. Please use another email."); 
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false); 
        String token  = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setEmail(user.getEmail());
        user.setUsername(user.getUsername());
        user.setHighestScore(0);
        user.setQuizSet(0);
        user.setUserXp(0);
        user.setLevel(1);
        user.setCreatedDate(new Date());
        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), token);

        return "redirect:/login";
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, Model model) {
        Users user = userRepository.findByVerificationToken(token);
        
        if (user == null) {
            model.addAttribute("message", "Invalid verification link!");
            return "error";
        }

        user.setEnabled(true);
        user.setVerificationToken(null); 
        userRepository.save(user);

        Users updatedUser = userRepository.findByUsername(user.getUsername());
        System.out.println("User after verification " + updatedUser.isEnabled());

        model.addAttribute("message", "Your email has been verified. You can now log in.");
        return "redirect:/login";
    }


    @GetMapping("/login")
    public String showLoginForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
    
            if (principal instanceof CustomOAuth2User) {
                System.out.println("This is redirect to home page and the principal is in OAuthUser" );
                return "redirect:/home";
            }
            if(principal instanceof UserDetails){
                System.out.println("This is redirect to home page and the principal is in Userdetails" );
                Users currentUser = userRepository.findByUsername(((UserDetails) principal).getUsername());
                if(currentUser.isEnabled()){
                    if(userRepository.findByEmail(currentUser.getEmail()) != null) {
                        System.out.println("This is the check point for the email.");
                        model.addAttribute("error", "Your email is already registered. Please use another email."); 
                        return "/register";
                    }
                }else{
                    System.out.println("User is not enabled");
                }
                return "redirect:/home";
            }
        }
        
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
                    
                    int quiz = currentUser.getQuizSet();
                    System.out.println("========================================");
                    System.out.println("This is the Custom Oauth2 User");
                    System.out.println("========================================");

                    model.addAttribute("quiz", quiz);

                } else if (principal instanceof UserDetails) {
                    UserDetails userDetails = (UserDetails) principal;
                    System.out.println(userDetails.getUsername());
                    Users currentUser = userRepository.findByUsername(userDetails.getUsername());
                    int quiz = currentUser.getQuizSet();
                    System.out.println("========================================");
                    System.out.println("This is the User Details User");
                    System.out.println("========================================");

                    if(currentUser.isEnabled()){
                        System.out.println("User is enabled");
                    } else {
                        System.out.println("User is not enabled");
                        model.addAttribute("error", "Please Verify your email to continue");
                        return "/login";
                    }
                    model.addAttribute("quiz", quiz);
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

        if(userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Username is already taken. Please choose another.");
            return "update_profile";
        }

        existingUser.setUsername(user.getUsername());
        // existingUser.setEmail(user.getEmail());

        userRepository.save(existingUser);
        model.addAttribute("user", existingUser);  

        
        return "redirect:/home"; 
    }

}