package com.EDTECH.QUIZ.GAME.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GameZoneController {
    
    @GetMapping("/gamezone")
    public String gameZone() {
        return "game_zone";
    }
}
