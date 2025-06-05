// package com.EDTECH.QUIZ.GAME.sevices;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.EDTECH.QUIZ.GAME.models.Flashcard;
// import com.EDTECH.QUIZ.GAME.repositories.FlashcardRepository;

// @Service
// public class FlashcardService {
    
//     @Autowired
//     private FlashcardRepository flashcardRepository;

//     @Autowired
//     private Flashcard flashcard;

//     public Flashcard createFlashcard(Flashcard flashcard) {
//         return flashcardRepository.save(flashcard);
//     }

//     public void deleteFlashcard(Long id) {
//         flashcardRepository.deleteById(id);
//     }

//     public Flashcard getFlashcardById(Long id) {
//         return flashcardRepository.findById(id).orElse(null);
//     }

//     public Flashcard updateFlashcard(Flashcard flashcard) {
//         return flashcardRepository.save(flashcard);
//     }

    
// }
