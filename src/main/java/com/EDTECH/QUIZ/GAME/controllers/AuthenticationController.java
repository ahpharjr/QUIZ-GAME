package com.EDTECH.QUIZ.GAME.controllers;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.EDTECH.QUIZ.GAME.models.PasswordResetToken;
import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.PasswordResetTokenRepository;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;
import com.EDTECH.QUIZ.GAME.sevices.CustomOAuth2User;
import com.EDTECH.QUIZ.GAME.sevices.EmailService;
import com.EDTECH.QUIZ.GAME.sevices.OtpService;
import com.EDTECH.QUIZ.GAME.sevices.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @GetMapping("/")
    public String landing() {
        return "landing";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        System.out.println("THis is the GET method for Forgot Password"); // Debugging log
        model.addAttribute("user", new Users());
        return "forgot_password";  
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@ModelAttribute("user") Users user, Model model) {
       System.out.println("This is in the post method if forgot password . ");
        String value = user.getEmail();
        String userEmail = value.split(",")[0]; 
       Users existingUser = userRepository.findByEmail(userEmail);
        System.out.println("This is the email of the user " + user.getEmail());
        System.out.println("===========================");
        System.out.println("This is th user find by email " + existingUser);
        System.out.println("===========================");
        if (existingUser == null) {
            model.addAttribute("errorMessage", "Email is not registered.");
            return "forgot_password";
        }  
        System.out.println("This is the email of the user " + userEmail);
        String otp = otpService.generateOtp(userEmail);
        System.out.println("+++++++++++++++++++");
        System.out.println("This is after the otp generation " + otp);
        emailService.sendOtpEmail(userEmail, otp);
        System.out.println("+++++++++++++++++++");
        System.out.println("This is after the email sent  ");
        model.addAttribute("email", userEmail);
        model.addAttribute("successMessage", "OTP has been sent to your email.");

        return "/forgot_password";
    }

    @PostMapping("/verify-otp")
    @ResponseBody
    public ResponseEntity<Map<String, String>> verifyOtp(
            @RequestParam("email") String email,
            @RequestParam("otp1") String otp1,
            @RequestParam("otp2") String otp2,
            @RequestParam("otp3") String otp3,
            @RequestParam("otp4") String otp4,
            @RequestParam("otp5") String otp5,
            @RequestParam("otp6") String otp6) {

        Map<String, String> response = new HashMap<>();

        // Trim and remove duplicate emails
        email = email.split(",")[0].trim();
        System.out.println("Cleaned email: " + email);

        if (email.isEmpty()) {
            response.put("success", "false");
            response.put("message", "Email is missing.");
            return ResponseEntity.badRequest().body(response);
        }

        String otp = otp1 + otp2 + otp3 + otp4 + otp5 + otp6;
        if (otp.isEmpty()) {
            response.put("success", "false");
            response.put("message", "OTP is required.");
            return ResponseEntity.badRequest().body(response);
        }

        Users user = userRepository.findByEmail(email);
        if (user == null) {
            response.put("success", "false");
            response.put("message", "No user found with this email.");
            return ResponseEntity.badRequest().body(response);
        }

        boolean isValid = otpService.verifyOtp(email, otp);
        if (!isValid) {
            response.put("success", "false");
            response.put("message", "OTP is incorrect. Please try again.");
            return ResponseEntity.ok(response);
        }

        PasswordResetToken existingtoken = passwordResetTokenRepository.findByUser(user);
            String token = UUID.randomUUID().toString();

        if (existingtoken != null) {
            existingtoken.setToken(token);
            existingtoken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
            passwordResetTokenRepository.save(existingtoken);
        } else{
            PasswordResetToken resetToken = new PasswordResetToken(token, user);
            passwordResetTokenRepository.save(resetToken);
        }

        response.put("success", "true");
        response.put("resetUrl", "/reset-password?token=" + token);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(Model model, @RequestParam("token") String token) {

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            model.addAttribute("error", "Invalid or expired reset token.");
            return "redirect:/forgot-password";
        }

        Users user = resetToken.getUser();

        model.addAttribute("userName", user.getUsername());
        model.addAttribute("userEmail", user.getEmail());
        model.addAttribute("token", token);
        return "reset_password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam("token") String token,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes) {

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        
        if (resetToken == null || resetToken.isExpired()) {
            redirectAttributes.addFlashAttribute("error", "Invalid or expired reset token.");
            return "redirect:/forgot-password";
        }

        Users user = resetToken.getUser();

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Password fields cannot be empty.");
            return "redirect:/reset-password?token=" + token;
        }

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/reset-password?token=" + token;
        }

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        // Delete token after use
        passwordResetTokenRepository.delete(resetToken);

        return "redirect:/login";
    }


    // @PostMapping("/verify-otp")
    // public String  verifyOtp(Model model,
    //         @RequestParam("email") String email,
    //         @RequestParam("otp1") String otp1,
    //         @RequestParam("otp2") String otp2,
    //         @RequestParam("otp3") String otp3,
    //         @RequestParam("otp4") String otp4,
    //         @RequestParam("otp5") String otp5,
    //         @RequestParam("otp6") String otp6) {
        
    //     String otp = otp1 + otp2 + otp3 + otp4 + otp5 + otp6; 
        
    //     System.out.println("This is inside of verify otp post mapping method");
    //     System.out.println("===========================");
    //     System.out.println("This is the email of the user inside the verify otp " + email);
    //     if (email == null || otp.isEmpty()) {
    //         System.out.println("if email==null >>>>>>>>>>>>>>>>>>>>>>");
    //         model.addAttribute("error", "Email and OTP are required.");
    //         return "/forgot-password";
    //     }

    //     boolean isValid = otpService.verifyOtp(email, otp);
    //     if (!isValid) {
    //         System.out.println("!isvalid?>>>>>>>>>>>>>>>>>>>>>>.");
    //         model.addAttribute("invalidOTP", "OPT is Incorrect.");
    //         model.addAttribute("user", new Users());
    //         return "forgot_password";
    //     }

    //     System.out.println("Before add email to model:::::::::::::::::::;;;;;");
    //     model.addAttribute("email", email);
    //     System.out.println("after add email to model:::::::::::::::::;;");
    //     return "redirect:/reset-password/" + email;
    // }

    // @GetMapping("/reset-password/{email}")
    // public String showResetPasswordForm(Model model, @PathVariable String email) {
    //     if (email == null) {
    //         model.addAttribute("error", "Email is required.");
    //         return "redirect:/forgot-password";
    //     }
    //     System.out.println("This is the email of the user inside get method of reset PW " + email);
    //     model.addAttribute("email", email);

    //     return "reset_password";
    // }

    // @PostMapping("/reset-password")
    // public String resetPassword(
    //         @RequestParam("email") String email,
    //         @RequestParam("password") String password,
    //         @RequestParam("confirmPassword") String confirmPassword,
    //         HttpServletResponse response,
    //         Model model) {

            
    //     System.out.println("This is inside the post mapping of reset password******************************");

    //     if (password.isEmpty() || confirmPassword.isEmpty()) {
    //         model.addAttribute("error", "Password and Confirm Password are required.");
    //         model.addAttribute("email", email);

    //         return "redirect:/reset-password/"+email;
    //     }

    //     if (!password.equals(confirmPassword)) {
    //         System.out.println("This is the check points for the password equal.");
    //         model.addAttribute("error", "Passwords do not match.");
    //         model.addAttribute("email", email);
    //         System.out.println("This is before redirect.");
    //         // return "reset_password";
    //         return "redirect:/reset-password/"+email;
    //     }
    //     System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
    //     System.out.println("This is the email of the user " + email);
    //     Users user = userRepository.findByEmail(email);
    //     System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
    //     System.out.println("This is the email of the user " + user.getEmail());
    //     user.setPassword(passwordEncoder.encode(password));
    //     System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
    //     System.out.println("THis is before user save to the database. ");
    //     System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
    //     userRepository.save(user);
    //     System.out.println("This is the password of the user " + user.getPassword());

    //     Cookie jwtCookie = new Cookie("JWT_TOKEN", null);
    //     jwtCookie.setHttpOnly(true);
    //     jwtCookie.setPath("/");
    //     jwtCookie.setMaxAge(0);
    //     response.addCookie(jwtCookie);

    //     return "redirect:/login";
    // }
    
    // @PostMapping("/reset-password")
    // public String resetPassword(
    //         @RequestParam("email") String email,
    //         @RequestParam("password") String password,
    //         @RequestParam("confirmPassword") String confirmPassword,
    //         HttpServletResponse response,
    //         RedirectAttributes redirectAttributes) {  // Use RedirectAttributes

    //     if (password.isEmpty() || confirmPassword.isEmpty()) {
    //         redirectAttributes.addFlashAttribute("error", "Password and Confirm Password are required.");
    //         return "redirect:/reset-password/" + email;
    //     }

    //     if (!password.equals(confirmPassword)) {
    //         redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
    //         return "redirect:/reset-password/" + email;
    //     }

    //     Users user = userRepository.findByEmail(email);
    //     user.setPassword(passwordEncoder.encode(password));
    //     userRepository.save(user);

    //     // Clear JWT cookie
    //     Cookie jwtCookie = new Cookie("JWT_TOKEN", null);
    //     jwtCookie.setHttpOnly(true);
    //     jwtCookie.setPath("/");
    //     jwtCookie.setMaxAge(0);
    //     response.addCookie(jwtCookie);

    //     return "redirect:/login";
    // }


    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new Users());
        System.out.println("This is the get mapping of register");
        return "register";
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") Users user, Model model, RedirectAttributes redirectAttributes) {

        if(userRepository.findByUsername(user.getUsername()) != null) {
            System.out.println("This is the post mapping of register Due to Username");
            model.addAttribute("error", "Username is already taken. Please choose another.");
            return "register";
        }else if(user.getUsername().contains("@")){
            System.out.println("This is the post mapping of register Due to Special Character '@'");
            model.addAttribute("error", "Username cannot contain '@'. Please choose another.");
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
        user.setTimeSpent(0);
        user.setQuizSet(0);
        user.setUserXp(0);
        user.setLevel(1);
        user.setCreatedDate(new Date());
        user.setCurrentQuizSet(1);
        user.setCurrentPhase(1);

        String profilePicture = userService.getProfileImage(user.getUserId());
        user.setPfPicture(profilePicture);

        //user.setEnabled(true);
        userRepository.save(user);

        System.out.println("User registered:>>>>>>>> " + user.getEmail() + " | Sending verification email...");

        emailService.sendVerificationEmail(user.getEmail(), token);

        System.out.println("Email sent process completed for:?>>>>>>> " + user.getEmail());

        // user.setEnabled(true);
        userRepository.save(user);

        // Store user email in flash attributes to be used on verification page
        // redirectAttributes.addFlashAttribute("userEmail", user.getEmail());

        //return "redirect:/login"; 
        return "redirect:/email-verification";
    }

    @GetMapping("/email-verification")
    public String emailVerification( Model model) {

        model.addAttribute("user", new Users());
        
        return "verify_email";
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, Model model) {
        Users user = userRepository.findByVerificationToken(token);
        
        if (user == null) {
            model.addAttribute("message", "Invalid verification link!");
            return "error";
        }
        System.out.println("User before verification " + user.isEnabled());
        user.setEnabled(true);
        user.setVerificationToken(null); 
        userRepository.save(user);
        System.out.println("User before verification " + user.isEnabled());


        Users updatedUser = userRepository.findByUsername(user.getUsername());
        System.out.println("User after verification " + updatedUser.isEnabled());

        model.addAttribute("message", "Your email has been verified. You can now log in.");
        return "redirect:/login";
    }

    @PostMapping("/resend-verification")
    public String resendVerificationEmail(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        Users user = userRepository.findByEmail(email);

        System.out.println("This is the email of the user in verification email::::::::::::::::::;; " + email);
        
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "No account found with this email.");
            return "redirect:/email-verification";
        }

        if (user.isEnabled()) {
            redirectAttributes.addFlashAttribute("error", "Your email is already verified. Please log in.");
            return "redirect:/email-verification";
        }

        // Generate a new token and resend the email
        String newToken = UUID.randomUUID().toString();
        user.setVerificationToken(newToken);
        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), newToken);
        redirectAttributes.addFlashAttribute("success", "A new verification link has been sent.");

        return "redirect:/email-verification";
    }

    @GetMapping("/login")
    public String showLoginForm( Model model) {

        System.out.println("This is the login page");
        System.out.println("==============================");
        System.out.println("This is before authentication");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication check point " + authentication);
        System.out.println("This is after authentication");
        System.out.println("==============================");
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
    
            if (principal instanceof CustomOAuth2User) {
                System.out.println("This is redirect to home page and the principal is in OAuthUser" );
                Users currentUser = userRepository.findByEmail(((CustomOAuth2User) principal).getEmail());
                model.addAttribute("profile", userService.getProfileImage(currentUser.getUserId())); 
                if(currentUser.isEnabled()){
                    if(userRepository.findByEmail(currentUser.getEmail()) != null) {
                        System.out.println("This is the check point for the email.");
                        model.addAttribute("error", "Your email is already registered. Please use another email."); 
                        return "login";
                    }
                }else{
                    System.out.println("User is not enabled");
                }
                return "redirect:/home";
            }
            if(principal instanceof UserDetails){
                System.out.println("This is redirect to home page and the principal is in Userdetails" );
                Users currentUser = userRepository.findByUsername(((UserDetails) principal).getUsername());

                if(currentUser.isEnabled()){
                    if(userRepository.findByEmail(currentUser.getEmail()) != null) {
                        System.out.println("This is the check point for the email.");
                        model.addAttribute("error", "Your email is already registered. Please use another email."); 
                        return "login";
                    }
                }else{
                    System.out.println("User is not enabled");
                }
                model.addAttribute("profile", userService.getProfileImage(currentUser.getUserId())); 
                return "redirect:/home";
            }
        }

        return "login";
        
    }

    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Users currentUser = userRepository.findByEmail(userDetails.getUsername());
        
        if (currentUser == null) {
            return "redirect:/login";  
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("profileImages", userService.getProfileImages());
        model.addAttribute("profile", userService.getProfileImage(currentUser.getUserId()));

        return "update_profile";  
    }

    @PostMapping("/edit") 
    public String editProfile(@ModelAttribute("user") Users user, Principal principal, Model model) {
        String email = principal.getName();
        Users existingUser = userRepository.findByEmail(email);

        if (existingUser == null) {
            return "redirect:/login";  
        }

        System.out.println("New profile picture received: " + user.getPfPicture());

        // Check if username is changed AND already taken
        if (!user.getUsername().equals(existingUser.getUsername()) 
            && userRepository.findByUsername(user.getUsername()) != null) {
               
            model.addAttribute("usernameError", "Username is already taken.");
            model.addAttribute("profileImages", userService.getProfileImages());
            model.addAttribute("user", existingUser);  // Keep old user data
           
            return "update_profile";  // Stay on the same page
        }

        // Update username only if it has changed
        if (!user.getUsername().equals(existingUser.getUsername())) {
            existingUser.setUsername(user.getUsername());
        }

        // Update profile picture only if it has changed (not null or empty)
        if (user.getPfPicture() != null && !user.getPfPicture().isEmpty()) {
            existingUser.setPfPicture(user.getPfPicture());
        }

        // Save changes
        userRepository.save(existingUser);

        return "redirect:/home"; 
    }


}