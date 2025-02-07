// package com.EDTECH.QUIZ.GAME.controllers;

// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import com.EDTECH.QUIZ.GAME.models.Users;
// import com.EDTECH.QUIZ.GAME.repositories.UserRepository;

// @Controller
// @RequestMapping("/verify-email")
// public class EmailVerificationController {

//     @Autowired
//     private UserRepository userRepository;

//     @GetMapping
//     public String verifyEmail(@RequestParam("token") String token, Model model) {
//         Optional<Users> userOptional = userRepository.findByVerificationToken(token);
        
//         if (userOptional.isPresent()) {
//             Users user = userOptional.get();
//             user.setEnabled(true); // Mark user as verified
//             user.setVerificationToken(null); // Remove token after verification
//             userRepository.save(user);
//             model.addAttribute("message", "Email verification successful!");
//             return "verification-success"; // This is a success page
//         } else {
//             model.addAttribute("message", "Invalid or expired token!");
//             return "verification-failed"; // This is a failure page
//         }
//     }
// }
