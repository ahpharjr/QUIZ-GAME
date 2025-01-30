package com.EDTECH.QUIZ.GAME.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.EDTECH.QUIZ.GAME.models.Collection;
import com.EDTECH.QUIZ.GAME.models.Flashcard;
import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.CollectionRepository;
import com.EDTECH.QUIZ.GAME.repositories.FlashcardRepository;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;

@Controller
public class CollectionController {

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/collection")
    public String collection(Model model, Principal principal) {
        if (principal != null) {
            Users user = userRepository.findByUsername(principal.getName());
            model.addAttribute("user", user);
        }

        return "card_collection";
    }

    @PostMapping("/collections/add")
    @ResponseBody
    public ResponseEntity<String> addCardToCollection(@RequestParam Long cardId, Principal principal) {
        Optional<Flashcard> flashcardOpt = flashcardRepository.findById(cardId);
        if (flashcardOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid card ID");
        }

        // Get currently logged-in user
        Users currentUser = userRepository.findByUsername(principal.getName());
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        List<Collection> existingCollections = collectionRepository.findByUser_UserId(currentUser.getUserId());
        boolean alreadyExists = existingCollections.stream()
            .anyMatch(c -> c.getFlashcard().getCardId().equals(cardId));

        if (alreadyExists) {
            return ResponseEntity.badRequest().body("Card already in collection");
        }

        Collection collection = new Collection();
        collection.setUser(currentUser);
        collection.setFlashcard(flashcardOpt.get());
        collectionRepository.save(collection);

        return ResponseEntity.ok("Card added to collection successfully");
    }

    @GetMapping("/collections/check")
    @ResponseBody
    public ResponseEntity<Boolean> isCardInCollection(@RequestParam Long cardId, Principal principal) {
        Users currentUser = userRepository.findByUsername(principal.getName());
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

        List<Collection> existingCollections = collectionRepository.findByUser_UserId(currentUser.getUserId());
        boolean isInCollection = existingCollections.stream()
            .anyMatch(c -> c.getFlashcard().getCardId().equals(cardId));

        return ResponseEntity.ok(isInCollection);
    }

    @GetMapping("/collections")
    @ResponseBody
    public ResponseEntity<List<Flashcard>> getUserCollections(Principal principal) {
        Users currentUser = userRepository.findByUsername(principal.getName());
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Collection> collections = collectionRepository.findByUser_UserId(currentUser.getUserId());
        List<Flashcard> flashcards = collections.stream()
                                                .map(Collection::getFlashcard)
                                                .toList();
        return ResponseEntity.ok(flashcards);
    }

    @PostMapping("/collections/remove")
    @ResponseBody
    public ResponseEntity<String> removeCardFromCollection(@RequestParam Long cardId, Principal principal) {
        Users currentUser = userRepository.findByUsername(principal.getName());
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        List<Collection> collections = collectionRepository.findByUser_UserId(currentUser.getUserId());
        Optional<Collection> collectionOpt = collections.stream()
                                                        .filter(c -> c.getFlashcard().getCardId().equals(cardId))
                                                        .findFirst();

        if (collectionOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Card not found in the collection");
        }

        collectionRepository.delete(collectionOpt.get());
        return ResponseEntity.ok("Card removed from collection successfully");
    }

}

