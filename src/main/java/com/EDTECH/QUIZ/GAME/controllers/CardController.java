package com.EDTECH.QUIZ.GAME.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;


import com.EDTECH.QUIZ.GAME.models.Flashcard;
import com.EDTECH.QUIZ.GAME.repositories.FlashcardRepository;
import com.EDTECH.QUIZ.GAME.repositories.TopicRepository;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;


@Controller
public class CardController {


    @Autowired
    private TopicRepository topicRepository;


    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("{phaseId}/cards")
    public String showCard(@PathVariable Long phaseId, Model model, Principal principal) {

        if (principal != null) {
            var user = userRepository.findByUsername(principal.getName());
            model.addAttribute("user", user);
        }
        // Fetch topics related to the selected phase
        var topics = topicRepository.findAllByPhasePhaseId(phaseId);
        model.addAttribute("topics", topics);

        // Load flashcards for the first topic (if any) as default
        if (!topics.isEmpty()) {
            var flashcards = flashcardRepository.findAllByTopicTopicId(topics.get(0).getTopicId());
            model.addAttribute("flashcards", flashcards);
        } else {
            model.addAttribute("flashcards", List.of());
        }

        return "card";
    }


    @GetMapping("/flashcards/{topicId}")
    @ResponseBody
    public List<Flashcard> getFlashcardsByTopic(@PathVariable Long topicId) {
        return flashcardRepository.findAllByTopicTopicId(topicId);
    }
}


