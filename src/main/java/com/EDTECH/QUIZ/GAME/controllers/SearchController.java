package com.EDTECH.QUIZ.GAME.controllers;

import java.util.List;

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
    public String search(@RequestParam(required = false, defaultValue = "") String keyword, Model model) {
        List<Flashcard> searchResults = List.of();
        Flashcard firstSearchCard = null;

        if (!keyword.isEmpty()) {
            // Fetch the first matching keyword from the suggestions
            List<String> suggestions = flashcardRepository.findKeywordsByFlexibleSearch(keyword);
            if (!suggestions.isEmpty()) {
                keyword = suggestions.get(0); // Use the first suggestion as the keyword
                searchResults = flashcardRepository.searchFlashcards(keyword);
                firstSearchCard = searchResults.get(0);
            }
        }

        model.addAttribute("searchResults", searchResults);
        model.addAttribute("firstSearchCard", firstSearchCard);
        model.addAttribute("keyword", keyword);

        return "search_card";
    }

    @ResponseBody
    @GetMapping("/suggest-keywords")
    public List<String> suggestKeywords(@RequestParam String prefix) {
        return flashcardRepository.findKeywordsByFlexibleSearch(prefix);
    }


}

