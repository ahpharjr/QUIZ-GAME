package com.EDTECH.QUIZ.GAME.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.EDTECH.QUIZ.GAME.repositories.PhaseRepository;

@Controller
public class LearningZoneController {
    @Autowired
    private PhaseRepository phaseRepository;
    
    @GetMapping("/learning-zone")
    public String showLearningPage(Model model){
        //Fetch all phases from the database
        var phases = phaseRepository.findAll();
        //Add phases to the
        model.addAttribute("phases", phases);

        return "learning_zone";
    }
 
}
