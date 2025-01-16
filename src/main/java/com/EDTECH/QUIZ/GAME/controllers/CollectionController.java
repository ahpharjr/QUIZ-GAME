package com.EDTECH.QUIZ.GAME.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CollectionController {
    
    @GetMapping("/collection")
    public String collection() {
        return "card_collection";
    }
}
