package com.EDTECH.QUIZ.GAME.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.EDTECH.QUIZ.GAME.models.Flashcard;
import com.EDTECH.QUIZ.GAME.repositories.FlashcardRepository;

@Controller
public class SearchController {

    @Autowired
    private FlashcardRepository flashcardRepository;
    
    @GetMapping("/search-card")
    public String search(@RequestParam(name= "keyword", required = false) 
                        String keyword, Model model) {
        
        List<Flashcard> searchResults = List.of(); // Default empty list
        if (keyword != null && !keyword.isBlank()) {
            searchResults = flashcardRepository.searchByKeyword(keyword);
        }
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("keyword", keyword); // Retain the keyword in the input field
         return "search_card";
    }

    @GetMapping("/search-suggestions")
    @ResponseBody
    public List<String> getSuggestions(@RequestParam String keyword) {
        return flashcardRepository.searchByKeyword(keyword).stream()
                .map(Flashcard::getKeyword)
                .collect(Collectors.toList());
    }

}
