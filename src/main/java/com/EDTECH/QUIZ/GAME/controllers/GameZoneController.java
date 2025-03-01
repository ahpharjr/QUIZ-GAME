package com.EDTECH.QUIZ.GAME.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.PhaseRepository;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;

@Controller
public class GameZoneController {
    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/gamezone")
    public String gameZone(Model model, Principal principal) {

        String email = principal.getName();
        Users user = userRepository.findByEmail(email);
        model.addAttribute("user", user);

        var phases = phaseRepository.findAll();
        model.addAttribute("phases", phases);

        return "game_zone";
    }
}
