package com.EDTECH.QUIZ.GAME.controllers;

import java.security.Principal;
import java.util.*;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.EDTECH.QUIZ.GAME.models.Answer;
import com.EDTECH.QUIZ.GAME.models.Leaderboard;
//import com.EDTECH.QUIZ.GAME.models.Leaderboard;
import com.EDTECH.QUIZ.GAME.models.Question;
import com.EDTECH.QUIZ.GAME.models.Quiz;
import com.EDTECH.QUIZ.GAME.models.QuizAttempt;
import com.EDTECH.QUIZ.GAME.models.QuizLeaderboard;
import com.EDTECH.QUIZ.GAME.models.UserAnswer;
import com.EDTECH.QUIZ.GAME.models.Users;
// import com.EDTECH.QUIZ.GAME.models.UserAnswer;
import com.EDTECH.QUIZ.GAME.repositories.AnswerRepository;
import com.EDTECH.QUIZ.GAME.repositories.LeaderboardRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuestionRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuizAttemptRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuizLeaderboardRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuizRepository;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;
import com.EDTECH.QUIZ.GAME.sevices.QuizAttemptService;
import com.EDTECH.QUIZ.GAME.sevices.UserAnswerService;

// import jakarta.websocket.server.PathParam;

@Controller
public class QuizController {
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserAnswerService userAnswerService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizAttemptService quizAttemptService;

    @Autowired
    private QuizLeaderboardRepository quizLeaderboardRepository;

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @GetMapping("/{topicId}/quiz")
    public String quiz(@PathVariable("topicId") Long topicId, Model model) {

        Quiz quiz = quizRepository.findQuizIdByTopic_TopicId(topicId);
        Long quizId = quiz.getQuizId();

        userAnswerService.startQuiz(quizId); // Start tracking quiz

        var questions = questionRepository.findByQuiz_QuizId(quizId);

        model.addAttribute( "quizId", quizId);
        model.addAttribute("questions", questions); // Pass all questions for context if needed
        model.addAttribute("currentQuestion", questions.get(0)); // Show the first question

        var answers = answerRepository.findByQuestion_QuestionId(questions.get(0).getQuestionId());
        model.addAttribute("answers", answers);

        // Add answer labels to model
        model.addAttribute("answerLabels", Arrays.asList("A", "B", "C", "D"));

        return "quiz";
    }

    @GetMapping("/quiz/next-question")
    @ResponseBody
    public ResponseEntity<?> nextQuestion(@RequestParam Long quizId, @RequestParam Long questionId) {
        List<Question> questions = questionRepository.findByQuiz_QuizId(quizId);

        int index = IntStream.range(0, questions.size())
            .filter(i -> questions.get(i).getQuestionId().equals(questionId))
            .findFirst()
            .orElse(-1);

        if (index == -1 || index + 1 >= 8) {
            return ResponseEntity.ok(Map.of("message", "Quiz completed"));
        }

        Question nextQuestion = questions.get(index + 1);
        List<Answer> nextAnswers = answerRepository.findByQuestion_QuestionId(nextQuestion.getQuestionId());

        return ResponseEntity.ok(Map.of(
            "question", nextQuestion,
            "answers", nextAnswers
        ));
    }


    @PostMapping("/quiz/submit-answer")
    @ResponseBody
    public ResponseEntity<?> submitAnswer(
        @RequestParam Long quizId, 
        @RequestParam Long questionId, 
        @RequestParam Long answerId, 
        Principal principal) {
        
        //var user = userRepository.findByUsername(principal.getName());
        var quiz = quizRepository.findById(quizId).orElse(null);
        
        if (quiz == null) {
            return ResponseEntity.badRequest().body("Invalid quiz.");
        }

        var question = questionRepository.findById(questionId).orElse(null);
        var answer = answerRepository.findById(answerId).orElse(null);

        if (question == null || answer == null || !question.getQuiz().equals(quiz)) {
            return ResponseEntity.badRequest().body("Invalid question or answer.");
        }

        boolean isCorrect = answer.isCorrect();
        userAnswerService.storeAnswer(questionId, answerId, isCorrect);

        System.out.println("user Answer service:::::::::::::::::::"+userAnswerService);
        
        return ResponseEntity.ok(Map.of("correct", isCorrect));
    }

    @GetMapping("/quiz/complete")
    @ResponseBody
    public ResponseEntity<?> completeQuiz(Principal principal) {
        Users user = userRepository.findByUsername(principal.getName());
        Long quizId = userAnswerService.getQuizId();

        if (quizId == null) {
            return ResponseEntity.badRequest().body("No active quiz found.");
        }

        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        if (quiz == null) {
            return ResponseEntity.badRequest().body("Quiz not found.");
        }

        int totalPoints = userAnswerService.calculatePoints();
        long timeTaken = userAnswerService.getTimeTaken();
        List<UserAnswer> userAnswers = userAnswerService.getUserAnswers();

        // Save the quiz attempt and user answers
        QuizAttempt quizAttempt = quizAttemptService.saveQuizAttempt(user, quiz, totalPoints, timeTaken, userAnswers);

        // Save only the final score in QuizLeaderboard
        QuizLeaderboard leaderboard = quizLeaderboardRepository.findByUserAndQuiz(user, quiz);

        if (leaderboard == null) {

            leaderboard = new QuizLeaderboard(totalPoints, timeTaken, user, quiz);

        } else {
            leaderboard.setPoint(totalPoints);
            leaderboard.setTimeTaken(timeTaken);
        }
        quizLeaderboardRepository.save(leaderboard);

        // Update points and time taken for phase leaderboard
        // Fetch user's phase leaderboard
        Leaderboard userPhaseLeaderboard = leaderboardRepository.findByUserAndPhase(user, quiz.getTopic().getPhase());

        if (userPhaseLeaderboard == null) { 
            userPhaseLeaderboard = new Leaderboard(0, 0L, user, quiz.getTopic().getPhase());
        }

        int currentPoints = userPhaseLeaderboard.getPoint();
        long currentTimeTaken = userPhaseLeaderboard.getTimeTaken();

        List<QuizAttempt> quizAttempts = quizAttemptRepository.findByUserAndQuiz(user, quiz);
        QuizAttempt lastAttempt = quizAttempts.get(quizAttempts.size()-1);

        int lastAttemptPoints = lastAttempt.getTotalPoints();
        long lastAttemptTimeTaken = lastAttempt.getTotalTime();

        // Find the highest points from all previous attempts (excluding the last one)
        int highestPreviousPoints = quizAttempts.stream()
                .limit(quizAttempts.size() - 1)  // Exclude the last attempt
                .mapToInt(QuizAttempt::getTotalPoints)
                .max()
                .orElse(0); // If no previous attempts, default to 0

        // Update only if the last attempt has the most points
        if (lastAttemptPoints > highestPreviousPoints) {
            int updatedPoints = currentPoints - highestPreviousPoints + lastAttemptPoints;
            long updatedTimeTaken = currentTimeTaken - 
                    quizAttempts.stream()
                        .filter(attempt -> attempt.getTotalPoints() == highestPreviousPoints)
                        .mapToLong(QuizAttempt::getTotalTime)
                        .findFirst()
                        .orElse(0L) 
                    + lastAttemptTimeTaken;

            userPhaseLeaderboard.setPoint(updatedPoints);
            userPhaseLeaderboard.setTimeTaken(updatedTimeTaken);
            leaderboardRepository.save(userPhaseLeaderboard); // Save only if updated
        } else if (quizAttempts.size() == 1) {
            // If it's the first attempt, always update
            userPhaseLeaderboard.setPoint(currentPoints + lastAttemptPoints);
            userPhaseLeaderboard.setTimeTaken(currentTimeTaken + lastAttemptTimeTaken);
            leaderboardRepository.save(userPhaseLeaderboard);
        }


        return ResponseEntity.ok(Map.of(
            "points", totalPoints,
            "timeTaken", timeTaken,
            "quizAttemptId", quizAttempt.getQuizAttemptId()
        ));
    }


}