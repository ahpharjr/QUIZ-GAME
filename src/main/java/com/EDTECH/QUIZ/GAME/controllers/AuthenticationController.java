package com.EDTECH.QUIZ.GAME.controllers;

import java.security.Principal;
import java.util.Date;

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

import jakarta.servlet.http.HttpServletResponse;


@Controller
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

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

        return "redirect:/home";
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

//     @PostMapping("/login")
// public String login(@RequestParam String username, 
//                     @RequestParam String password, 
//                     HttpServletResponse response, 
//                     Model model) {
//     Authentication authentication = authenticationManager.authenticate(
//             new UsernamePasswordAuthenticationToken(username, password)
//     );

//     SecurityContextHolder.getContext().setAuthentication(authentication);
//     UserDetails userDetails = (UserDetails) authentication.getPrincipal();

//     String jwtToken = jwtService.generateToken(userDetails);

//     // Store token in an HTTP-only secure cookie
//     Cookie jwtCookie = new Cookie("JWT_TOKEN", jwtToken);
//     jwtCookie.setHttpOnly(true); // Prevents JavaScript access
//     jwtCookie.setSecure(true); // Ensures HTTPS only
//     jwtCookie.setPath("/"); // Accessible throughout the app
//     jwtCookie.setMaxAge((int) jwtService.getExpirationTime() / 1000);

//     response.addCookie(jwtCookie);

//     return "redirect:/home";
// }

//     @GetMapping("/logout")
//     public String logout(HttpServletResponse response) {
//         Cookie jwtCookie = new Cookie("JWT_TOKEN", "");
//         jwtCookie.setHttpOnly(true);
//         jwtCookie.setSecure(true);
//         jwtCookie.setPath("/");
//         jwtCookie.setMaxAge(0); // Expire immediately

//         response.addCookie(jwtCookie);
//         return "redirect:/login";
//     }


}