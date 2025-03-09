// package com.EDTECH.QUIZ.GAME.controllers;

// import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.EDTECH.QUIZ.GAME.repositories.UserRepository;
// import com.EDTECH.QUIZ.GAME.sevices.EmailService;
// import com.EDTECH.QUIZ.GAME.sevices.OtpService;

// @RestController
// @RequestMapping("/api/auth/")
// public class RestControllerForOtp {

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private OtpService otpService;

//     @Autowired
//     private EmailService emailService;

//     @PostMapping("/forgot-password")
//     public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) { 
//         String email = request.get("email");
//         if (email == null || email.isEmpty()) {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                 .body(Map.of("message", "Email is required", "status", false));
//         }
    
//         String otp = otpService.generateOtp(email);
//         otpService.sendOtpToEmail(email, otp);
    
//         return ResponseEntity.ok(Map.of("message", "OTP sent to email", "status", true));
//     }

//     @PostMapping("/verify-otp")
//     public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
//         String email = request.get("email");
//         String otp = request.get("otp");

//         if (email == null || otp == null) {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                 .body(Map.of("message", "Email and OTP are required", "status", false));
//         }

//         boolean isValid = otpService.verifyOtp(email, otp);
//         if (!isValid) {
//             return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                 .body(Map.of("message", "Invalid OTP", "status", false));
//         }

//         return ResponseEntity.ok(Map.of("message", "OTP verified", "status", true));
//     }

    

//     // @PostMapping("/verify-otp")
//     // public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
//     //     String email = request.get("email");
//     //     String otp = request.get("otp");

//     //     boolean isValid = otpService.verifyOtp(email, otp);
//     //     if (!isValid) {
//     //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//     //             .body(Map.of("message", "Invalid OTP", "status", false));
//     //     }

//     //     return ResponseEntity.ok(Map.of("message", "OTP verified", "status", true));
//     // }

//     // @PostMapping("/send-otp")
//     // public ResponseEntity<String> sendOtp(@RequestParam String email) {
        
//     //     System.out.println("This is the send otp method: " + email);

//     //     if (userRepository.findByEmail(email) == null) {
//     //         return ResponseEntity.badRequest().body("Email is not registered.");
//     //     }

//     //     String otp = otpService.generateOtp(email);
//     //     emailService.sendOtpEmail(email, otp);
        
//     //     return ResponseEntity.ok("OTP has been sent to your email.");
//     // }

//     // @PostMapping("/verify-otp")
//     // public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
//     //     System.out.println("This is the verify otp method");

//     //     if (otpService.isValidOtp(email, otp)) {
//     //         otpService.clearOtp(email);
//     //         return ResponseEntity.ok("OTP Verified. You can now reset your password.");
//     //     }
//     //     return ResponseEntity.badRequest().body("Invalid OTP. Please try again.");
//     // }


// }
