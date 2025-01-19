package com.EDTECH.QUIZ.GAME.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.EDTECH.QUIZ.GAME.repositories.PhaseRepository;

@Controller
public class GameZoneController {
    @Autowired
    private PhaseRepository phaseRepository;
    
    @GetMapping("/gamezone")
    public String gameZone(Model model) {
        var phases = phaseRepository.findAll();
        model.addAttribute("phases", phases);

        return "game_zone";
    }
}
