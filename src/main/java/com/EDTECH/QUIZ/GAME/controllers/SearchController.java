package com.EDTECH.QUIZ.GAME.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {
    
    @GetMapping("/search-card")
    public String search() {
        return "search_card";
    }
}
