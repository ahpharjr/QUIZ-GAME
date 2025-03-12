package com.EDTECH.QUIZ.GAME.controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.EDTECH.QUIZ.GAME.models.UserAchievement;
import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.UserAchievementRepository;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;
import com.EDTECH.QUIZ.GAME.sevices.CustomOAuth2User;
import com.EDTECH.QUIZ.GAME.sevices.UserPerformanceService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    
    @Autowired
    private UserPerformanceService userPerformanceService;

    @Autowired
    private UserAchievementRepository userAchievementRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            Users currentUser = null;

            if (principal instanceof CustomOAuth2User) {
                currentUser = userRepository.findByEmail(((CustomOAuth2User) principal).getEmail());
            } else if (principal instanceof UserDetails) {
                currentUser = userRepository.findByEmail(((UserDetails) principal).getUsername());
            }

            if (currentUser != null) {

                int userXp = currentUser.getUserXp();
                int level = 1;
                int xpStart = 0;
                int xpEnd = 1200;

                if (userXp >= 1200 && userXp < 3200) {
                    level = 2;
                    xpStart = 1200;
                    xpEnd = 3200;
                    currentUser.setLevel(level);
                    userRepository.save(currentUser);
                } else if (userXp >= 3200 && userXp < 6200) {
                    level = 3;
                    xpStart = 3200;
                    xpEnd = 6200;
                    currentUser.setLevel(level);
                    userRepository.save(currentUser);
                } else if(userXp >= 6200 && userXp < 10000) {
                    level = 4;
                    xpStart = 6200;
                    xpEnd = 10000;
                    currentUser.setLevel(level);
                    userRepository.save(currentUser);
                } else if(userXp >= 10000) {
                    level = 5;
                    xpStart = 10000;
                    xpEnd = 10000;
                    currentUser.setLevel(level);
                    userRepository.save(currentUser);
                }

                int progressPercentage = (int) (((double) (userXp - xpStart) / (xpEnd - xpStart)) * 100);
                String formatTimeSpent = userPerformanceService.formatTimeSpent(currentUser.getTimeSpent());

                model.addAttribute("formatTimeSpent", formatTimeSpent);
                model.addAttribute("user", currentUser);
                model.addAttribute("xpStart", xpStart);
                model.addAttribute("xpEnd", xpEnd);
                model.addAttribute("progressPercentage", progressPercentage);

                Users user = currentUser;
                UserAchievement userAchievement = userAchievementRepository.findByUser(user)
                    .orElseGet(() -> userPerformanceService.createUserAchievement(user));

                userPerformanceService.updateAchievement(currentUser);

                Set<String> unlockedAchievements = new HashSet<>();

                if (userAchievement.getAchievePhase1() && !userAchievement.getDisplayedPhase1()) {
                    unlockedAchievements.add("Phase 1");
                    userAchievement.setDisplayedPhase1(true); // Mark as displayed
                }
                if (userAchievement.getAchievePhase2() && !userAchievement.getDisplayedPhase2()) {
                    unlockedAchievements.add("Phase 2");
                    userAchievement.setDisplayedPhase2(true);
                }
                if (userAchievement.getAchievePhase3() && !userAchievement.getDisplayedPhase3()) {
                    unlockedAchievements.add("Phase 3");
                    userAchievement.setDisplayedPhase3(true);
                }
                if (userAchievement.getAchievePhase4() && !userAchievement.getDisplayedPhase4()) {
                    unlockedAchievements.add("Phase 4");
                    userAchievement.setDisplayedPhase4(true);
                }
                if (userAchievement.getAchieveFirstAchiever() && !userAchievement.getDisplayedFirstAchiever()) {
                    unlockedAchievements.add("First Achiever");
                    userAchievement.setDisplayedFirstAchiever(true);
                }
                if (userAchievement.getAchieveRisingStar() && !userAchievement.getDisplayedRisingStar()) {
                    unlockedAchievements.add("Rising Star");
                    userAchievement.setDisplayedRisingStar(true);
                }
                if (userAchievement.getAchieveRookieQuizzer() && !userAchievement.getDisplayedRookieQuizzer()) {
                    unlockedAchievements.add("Rookie Quizzer");
                    userAchievement.setDisplayedRookieQuizzer(true);
                }
                if (userAchievement.getAchieveGenius() && !userAchievement.getDisplayedGenius()) {
                    unlockedAchievements.add("Genius");
                    userAchievement.setDisplayedGenius(true);
                }

                // Save updated displayed status
                userAchievementRepository.save(userAchievement);

                model.addAttribute("userAchievement", userAchievement);
                model.addAttribute("unlockedAchievements", unlockedAchievements);
            }
        }

        return "home";
    }


    // @GetMapping("/home")
    //     public String home(Model model, HttpSession session) {
    //         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //         System.out.println("This is the home page");
    //         System.out.println("==============================");
    //         System.out.println("authentication check point " + authentication);

    //         if (authentication != null) {
    //             Object principal = authentication.getPrincipal();
    //             Users currentUser = null;

    //             if (principal instanceof CustomOAuth2User) {
    //                 System.out.println("This is the Custom Oauth2 User");
    //                 System.out.println("========================================");
    //                 System.out.println("This is the email value to find in the databse => " + ((CustomOAuth2User) principal).getEmail());
    //                 System.out.println("========================================");
    //                 currentUser = userRepository.findByEmail(((CustomOAuth2User) principal).getEmail());
    //             } else if (principal instanceof UserDetails) {
    //                 System.out.println("This is the User Details User");
    //                 System.out.println("========================================");
    //                 System.out.println("This is the email value to find in the databse => " + ((UserDetails) principal).getUsername());
    //                 System.out.println("========================================");
    //                 currentUser = userRepository.findByEmail(((UserDetails) principal).getUsername());
    //                 if (!currentUser.isEnabled()) {
    //                     model.addAttribute("error", "Please Verify your email to continue");
    //                     return "/login";
    //                 }
    //             }else{
    //                 System.out.println("This is the else statement");
    //                 System.out.println(" The User is nether of the Type.");
    //             }

    //             if (currentUser != null) {
    //                 int userXp = currentUser.getUserXp();
    //                 int level = 1;
    //                 int xpStart = 0;
    //                 int xpEnd = 1200;

    //                 if (userXp >= 1200 && userXp < 3200) {
    //                     level = 2;
    //                     xpStart = 1200;
    //                     xpEnd = 3200;
    //                     currentUser.setLevel(level);
    //                     userRepository.save(currentUser);
    //                 } else if (userXp >= 3200 && userXp < 6200) {
    //                     level = 3;
    //                     xpStart = 3200;
    //                     xpEnd = 6200;
    //                     currentUser.setLevel(level);
    //                     userRepository.save(currentUser);
    //                 } else if(userXp >= 6200 && userXp < 10000) {
    //                     level = 4;
    //                     xpStart = 6200;
    //                     xpEnd = 10000;
    //                     currentUser.setLevel(level);
    //                     userRepository.save(currentUser);
    //                 } else if(userXp >= 10000) {
    //                     level = 5;
    //                     xpStart = 10000;
    //                     xpEnd = 10000;
    //                     currentUser.setLevel(level);
    //                     userRepository.save(currentUser);
    //                 }

    //                 int progressPercentage = (int) (((double) (userXp - xpStart) / (xpEnd - xpStart)) * 100);
    //                 String formatTimeSpent = userPerformanceService.formatTimeSpent(currentUser.getTimeSpent());

    //                 model.addAttribute("formatTimeSpent", formatTimeSpent);
    //                 model.addAttribute("user", currentUser);
    //                 model.addAttribute("xpStart", xpStart);
    //                 model.addAttribute("xpEnd", xpEnd);
    //                 model.addAttribute("progressPercentage", progressPercentage);

    //                 Users user = currentUser;

    //                 UserAchievement userAchievement = userAchievementRepository.findByUser(user)
    //                     .orElseGet(() -> userPerformanceService.createUserAchievement(user));
                    
    //                 userPerformanceService.updateAchievement(user);

    //                 //session.removeAttribute("displayedAchievements");

    //                 // Check newly unlocked achievements
    //                 Set<String> unlockedAchievements = new HashSet<>();
    //                 Set<String> displayedAchievements = (Set<String>) session.getAttribute("displayedAchievements");

    //                 if (displayedAchievements == null) {
    //                     displayedAchievements = new HashSet<>(); 
    //                 }

    //                 if (userAchievement.getAchievePhase1() && !displayedAchievements.contains("Phase 1")) {
    //                     unlockedAchievements.add("Phase 1");
    //                     System.out.println("Added Phase 1 Achievement!");
    //                 }
    //                 if (userAchievement.getAchievePhase2() && !displayedAchievements.contains("Phase 2")) {
    //                     unlockedAchievements.add("Phase 2");
    //                     System.out.println("Added Phase 2 Achievement!");
    //                 }
    //                 if (userAchievement.getAchievePhase3() && !displayedAchievements.contains("Phase 3")) {
    //                     unlockedAchievements.add("Phase 3");
    //                     System.out.println("Added Phase 3 Achievement!");
    //                 }
    //                 if (userAchievement.getAchievePhase4() && !displayedAchievements.contains("Phase 4")) {
    //                     unlockedAchievements.add("Phase 4");
    //                     System.out.println("Added Phase 4 Achievement!");
    //                 }
    //                 if (userAchievement.getAchieveFirstAchiever() && !displayedAchievements.contains("First Achiever")) {
    //                     unlockedAchievements.add("First Achiever");
    //                     System.out.println("Added First Achiever Achievement!");
    //                 }
    //                 if (userAchievement.getAchieveRisingStar() && !displayedAchievements.contains("Rising Star")) {
    //                     unlockedAchievements.add("Rising Star");
    //                     System.out.println("Added Rising Star Achievement!");
    //                 }
    //                 if (userAchievement.getAchieveRookieQuizzer() && !displayedAchievements.contains("Rookie Quizzer")) {
    //                     unlockedAchievements.add("Rookie Quizzer");
    //                     System.out.println("Added Rookie Quizzer Achievement!");
    //                 }
    //                 if (userAchievement.getAchieveGenius() && !displayedAchievements.contains("Genius")) {
    //                     unlockedAchievements.add("Genius");
    //                     System.out.println("Added Genius Achievement!");
    //                 }
                    
    //                 System.out.println("Final Unlocked Achievements: " + unlockedAchievements);

    //                 // Update displayed achievements
    //                 displayedAchievements.addAll(unlockedAchievements);
    //                 session.setAttribute("displayedAchievements", displayedAchievements);

    //                 model.addAttribute("userAchievement", userAchievement);

    //                 System.out.println("User Achievement: " + userAchievement);
    //                 System.out.println("Displayed Achievements from Session: " + displayedAchievements);
    //                 System.out.println("Unlocked Achievements before adding: " + unlockedAchievements);


    //                 model.addAttribute("unlockedAchievements", unlockedAchievements);

    //                 int timeout = session.getMaxInactiveInterval(); // Returns time in seconds
    //                 System.out.println("Session timeout:>>>>>>>>>>>>>>>>>>>>> " + timeout + " seconds");

    //             }
    //         }

    //         return "home";
    //     }  

}
