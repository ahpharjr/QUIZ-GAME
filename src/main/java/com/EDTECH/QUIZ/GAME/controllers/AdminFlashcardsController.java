// package com.EDTECH.QUIZ.GAME.controllers;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.RestController;

// import com.EDTECH.QUIZ.GAME.models.Flashcard;
// import com.EDTECH.QUIZ.GAME.models.Topic;
// import com.EDTECH.QUIZ.GAME.repositories.FlashcardRepository;
// import com.EDTECH.QUIZ.GAME.repositories.TopicRepository;
// import com.EDTECH.QUIZ.GAME.sevices.FlashcardService;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;


// @RestController
// @RequestMapping("/admin/flashcard")
// public class AdminFlashcardsController {
    
//     @Autowired
//     private FlashcardService flashcardService;

//     @Autowired
//     private TopicRepository topicRepository;
    
//     @GetMapping("/create")
//     public String getMethodName(
//         @RequestParam String keyword , 
//         @RequestParam String answer , 
//         @RequestParam String image ,
//         @RequestParam Long topicId) {

//         String imageName = "/images/flashcards/" +  image;

//         Topic topic = topicRepository.findById(topicId).orElse(null);
//         Flashcard newCard = new Flashcard(keyword, answer , imageName , topic);

//         flashcardService.createFlashcard(newCard);
//         return "Flashcard created successfully";
//     }

//     @GetMapping("/delete")
//     public String deleteFlashcard(@RequestParam Long id) {
//         flashcardService.deleteFlashcard(id);
//         return "Flashcard deleted successfully";
//     }

//     @GetMapping("/update")
//     public Flashcard updateFlashcard(
//         @RequestParam Long id , 
//         @RequestParam String keyword , 
//         @RequestParam String answer , 
//         @RequestParam String image ,
//         @RequestParam Long topicId) {

//         Flashcard flashcard = flashcardService.getFlashcardById(id);
//         flashcard.setKeyword(keyword);
//         flashcard.setDefinition(answer);
//         flashcard.setImage(image);
        
//         Topic topic = topicRepository.findById(topicId).orElse(null);
//         flashcard.setTopic(topic);

//         flashcardService.updateFlashcard(flashcard);

//         // return "Flashcard updated successfully";

//         return flashcard;
//     }

// }
