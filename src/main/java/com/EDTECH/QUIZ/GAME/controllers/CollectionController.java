package com.EDTECH.QUIZ.GAME.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.EDTECH.QUIZ.GAME.models.Collection;
import com.EDTECH.QUIZ.GAME.models.Flashcard;
import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.CollectionRepository;
import com.EDTECH.QUIZ.GAME.repositories.FlashcardRepository;

@Controller
public class CollectionController {

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private FlashcardRepository flashcardRepository;

    @GetMapping("/collection")
    public String collection() {
        return "card_collection";
    }

    @PostMapping("/collections/add")
    @ResponseBody
    public ResponseEntity<String> addCardToCollection(@RequestParam Long cardId, @RequestParam Long userId) {
        Optional<Flashcard> flashcardOpt = flashcardRepository.findById(cardId);
        if (flashcardOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid card ID");
        }

        List<Collection> existingCollections = collectionRepository.findByUser_UserId(userId);
        boolean alreadyExists = existingCollections.stream()
            .anyMatch(c -> c.getFlashcard().getCardId().equals(cardId));

        if (alreadyExists) {
            return ResponseEntity.badRequest().body("Card already in collection");
        }

        // Create and save the new collection entry
        Collection collection = new Collection();
        collection.setUser(new Users(1)); // Assuming userId is passed
        collection.setFlashcard(flashcardOpt.get());
        collectionRepository.save(collection);

        return ResponseEntity.ok("Card added to collection successfully");
    }


    @GetMapping("/collections/check")
    @ResponseBody
    public ResponseEntity<Boolean> isCardInCollection(@RequestParam Long cardId, @RequestParam Long userId) {
        List<Collection> existingCollections = collectionRepository.findByUser_UserId(userId);
        boolean isInCollection = existingCollections.stream()
            .anyMatch(c -> c.getFlashcard().getCardId().equals(cardId));

        return ResponseEntity.ok(isInCollection);
    }

    @GetMapping("/collections")
    @ResponseBody
    public ResponseEntity<List<Flashcard>> getUserCollections(@RequestParam Long userId) {
        List<Collection> collections = collectionRepository.findByUser_UserId(userId);
        List<Flashcard> flashcards = collections.stream()
                                                .map(Collection::getFlashcard)
                                                .toList();
        return ResponseEntity.ok(flashcards);
    }

    @PostMapping("/collections/remove")
    @ResponseBody
    public ResponseEntity<String> removeCardFromCollection(@RequestParam Long cardId, @RequestParam Long userId) {
        List<Collection> collections = collectionRepository.findByUser_UserId(userId);

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

