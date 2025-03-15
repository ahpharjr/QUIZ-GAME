package com.EDTECH.QUIZ.GAME.controllers;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired; // Import HttpSession
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.EDTECH.QUIZ.GAME.models.Answer;
import com.EDTECH.QUIZ.GAME.models.Leaderboard;
import com.EDTECH.QUIZ.GAME.models.Phase;
import com.EDTECH.QUIZ.GAME.models.Question;
import com.EDTECH.QUIZ.GAME.models.Quiz;
import com.EDTECH.QUIZ.GAME.models.QuizAttempt;
import com.EDTECH.QUIZ.GAME.models.QuizLeaderboard;
import com.EDTECH.QUIZ.GAME.models.UserAnswer;
import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.AnswerRepository;
import com.EDTECH.QUIZ.GAME.repositories.LeaderboardRepository;
import com.EDTECH.QUIZ.GAME.repositories.PhaseRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuestionRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuizAttemptRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuizLeaderboardRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuizRepository;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;
import com.EDTECH.QUIZ.GAME.sevices.QuizAttemptService;
import com.EDTECH.QUIZ.GAME.sevices.UserAnswerService;
import com.EDTECH.QUIZ.GAME.sevices.UserPerformanceService;

import jakarta.servlet.http.HttpSession;

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

    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private UserPerformanceService userPerformanceService;

    @GetMapping("/{topicId}/quiz")
    public String quiz(@PathVariable("topicId") Long topicId, Model model, Principal principal, HttpSession session) {
        String email = principal.getName();
        Users user = userRepository.findByEmail(email);
        model.addAttribute("user", user);
        
        Quiz quiz = quizRepository.findQuizIdByTopic_TopicId(topicId);
        if (quiz == null) {
            return "error"; // Handle case where no quiz exists
        }

        Long quizId = quiz.getQuizId();
        
        // Check if start time is already stored in the session
        Long sessionStartTime = (Long) session.getAttribute("startTime");
        if (sessionStartTime == null) {
            sessionStartTime = System.currentTimeMillis();
            session.setAttribute("startTime", sessionStartTime); // Store start time in session
        }

        userAnswerService.startQuiz(quizId, sessionStartTime); // Pass sessionStartTime to service

        List<Question> selectedQuestions = (List<Question>) session.getAttribute("selectedQuestions");
        Integer currentQuestionIndex = (Integer) session.getAttribute("currentQuestionIndex");
        Map<Long, Long> userAnswers = (Map<Long, Long>) session.getAttribute("userAnswers");

        if (selectedQuestions == null || selectedQuestions.isEmpty()) {
            List<Question> allQuestions = questionRepository.findByQuiz_QuizId(quizId);
            Collections.shuffle(allQuestions);
            selectedQuestions = allQuestions.stream().limit(10).collect(Collectors.toList());
            session.setAttribute("selectedQuestions", selectedQuestions);
            currentQuestionIndex = 0;
            session.setAttribute("currentQuestionIndex", currentQuestionIndex);
        }

        if (userAnswers == null) {
            userAnswers = new HashMap<>();
            session.setAttribute("userAnswers", userAnswers);
        } else {
            for (Map.Entry<Long, Long> entry : userAnswers.entrySet()) {
                Long questionId = entry.getKey();
                Long answerId = entry.getValue();
                boolean isCorrect = answerRepository.findById(answerId)
                                        .map(Answer::isCorrect)
                                        .orElse(false);

                userAnswerService.storeAnswer(questionId, answerId, isCorrect);
            }
        }

        int questionCount = selectedQuestions.size();
        Phase phase = phaseRepository.findByTopics_TopicId(topicId);
        model.addAttribute("phase", phase);
        model.addAttribute("quizId", quizId);
        model.addAttribute("questions", selectedQuestions);
        model.addAttribute("currentQuestion", selectedQuestions.get(currentQuestionIndex));
        model.addAttribute("question", selectedQuestions.get(currentQuestionIndex));
        model.addAttribute("questionCount", questionCount);

        List<Answer> answers = answerRepository.findByQuestion_QuestionId(selectedQuestions.get(currentQuestionIndex).getQuestionId());


        Collections.shuffle(answers);
        model.addAttribute("answers", answers);
        model.addAttribute("answerLabels", Arrays.asList("A", "B", "C", "D"));
        model.addAttribute("currentQuestionIndex", currentQuestionIndex + 1); // 1-based index for display
        model.addAttribute("userAnswers", userAnswers);

        return "quiz";
    }


    @GetMapping("/quiz/next-question")
    @ResponseBody
    public ResponseEntity<?> nextQuestion(@RequestParam Long questionId, Principal principal, HttpSession session) {
        List<Question> selectedQuestions = (List<Question>) session.getAttribute("selectedQuestions");
        Integer currentQuestionIndex = (Integer) session.getAttribute("currentQuestionIndex");

        if (selectedQuestions == null || selectedQuestions.isEmpty() || currentQuestionIndex == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Quiz session not found.")); // Return JSON here
        }    

        if (currentQuestionIndex >= selectedQuestions.size() - 1) {
            return ResponseEntity.ok(Map.of("message", "Quiz completed"));
        }

        currentQuestionIndex++;
        session.setAttribute("currentQuestionIndex", currentQuestionIndex); // Store progress

        Question nextQuestion = selectedQuestions.get(currentQuestionIndex);
        List<Answer> nextAnswers = answerRepository.findByQuestion_QuestionId(nextQuestion.getQuestionId());
        Collections.shuffle(nextAnswers);

        return ResponseEntity.ok(Map.of(
            "question", nextQuestion,
            "answers", nextAnswers,
            "currentQuestionIndex", currentQuestionIndex + 1 // 1-based index
        ));
    }

    @PostMapping("/quiz/submit-answer")
    @ResponseBody
    public ResponseEntity<?> submitAnswer(
        @RequestParam Long quizId, 
        @RequestParam Long questionId, 
        @RequestParam(value = "answerId", required = false) Optional<Long> answerIdOptional, 
        Principal principal, 
        HttpSession session) {
    
        Long answerId = answerIdOptional.orElse(null);
    
        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        if (quiz == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid quiz."));
        }
    
        Question question = questionRepository.findById(questionId).orElse(null);
        if (question == null || !question.getQuiz().equals(quiz)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid question."));
        }
    
        boolean isCorrect = false;
        if (answerId != null) {
            Answer answer = answerRepository.findById(answerId).orElse(null);
            if (answer != null) {
                isCorrect = answer.isCorrect();
            }
        }
    
        userAnswerService.storeAnswer(questionId, answerId, isCorrect);
    
        Map<Long, Long> userAnswers = (Map<Long, Long>) session.getAttribute("userAnswers");
        if (userAnswers == null) {
            userAnswers = new HashMap<>();
        }
    
        userAnswers.put(questionId, answerId != null ? answerId : -1L); // Store -1 for unanswered
        session.setAttribute("userAnswers", userAnswers);
    
        return ResponseEntity.ok(Map.of("correct", isCorrect));
    }


    @GetMapping("/quiz/complete")
    @ResponseBody
    public ResponseEntity<?> completeQuiz(Principal principal, HttpSession session) { 
        Users user = userRepository.findByEmail(principal.getName());
        Long quizId = userAnswerService.getQuizId();

        if (quizId == null) {
            return ResponseEntity.badRequest().body("No active quiz found.");
        }

        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        if (quiz == null) {
            return ResponseEntity.badRequest().body("Quiz not found.");
        }

        int totalPoints = userAnswerService.calculatePoints();
        long timeTaken = userAnswerService.getTimeTaken(session); // Use session-based time
        List<UserAnswer> userAnswers = userAnswerService.getUserAnswers();
        int bonusXp = userAnswerService.calculateCorrectAnswerPercentage();

        QuizAttempt quizAttempt = quizAttemptService.saveQuizAttempt(user, quiz, totalPoints, timeTaken, userAnswers, bonusXp);

        QuizLeaderboard leaderboard = quizLeaderboardRepository.findByUserAndQuiz(user, quiz);

        if (leaderboard == null) {
            leaderboard = new QuizLeaderboard(totalPoints, timeTaken, user, quiz);
        } else {
            leaderboard.setPoint(totalPoints);
            leaderboard.setTimeTaken(timeTaken);
        }
        quizLeaderboardRepository.save(leaderboard);

        // Update user phase leaderboard
        Leaderboard userPhaseLeaderboard = leaderboardRepository.findByUserAndPhase(user, quiz.getTopic().getPhase());

        if (userPhaseLeaderboard == null) { 
            userPhaseLeaderboard = new Leaderboard(0, 0L, user, quiz.getTopic().getPhase());
        }

        int currentPoints = userPhaseLeaderboard.getPoint();
        long currentTimeTaken = userPhaseLeaderboard.getTimeTaken();

        List<QuizAttempt> quizAttempts = quizAttemptRepository.findByUserAndQuiz(user, quiz);

        QuizAttempt lastAttempt = quizAttempts.get(quizAttempts.size() - 1);

        int lastAttemptPoints = lastAttempt.getTotalPoints();
        long lastAttemptTimeTaken = lastAttempt.getTotalTime();

        int highestPreviousPoints = quizAttempts.stream()
                .limit(quizAttempts.size() - 1)
                .mapToInt(QuizAttempt::getTotalPoints)
                .max()
                .orElse(0);

                System.out.println("highestPreviousPoints::::::::::::::::::::>>>>>>> " + highestPreviousPoints);  

        System.out.println("Correct Answers Percentage::::::::::::::::::::>>>>>>> " + userAnswerService.calculateCorrectAnswerPercentage());

        if (lastAttemptPoints > highestPreviousPoints) {
            System.out.println("lastAttemptPoints::::::::::::::::::::>>>>>>> " + lastAttemptPoints);
            int updatedPoints = currentPoints - highestPreviousPoints + lastAttemptPoints;
            System.out.println("updatedPoints::::::::::::::::::::>>>>>>> " + updatedPoints);
            long updatedTimeTaken = currentTimeTaken -
                    quizAttempts.stream()
                            .filter(attempt -> attempt.getTotalPoints() == highestPreviousPoints)
                            .mapToLong(QuizAttempt::getTotalTime)
                            .findFirst()
                            .orElse(0L)
                    + lastAttemptTimeTaken;

            userPhaseLeaderboard.setPoint(updatedPoints);
            userPhaseLeaderboard.setTimeTaken(updatedTimeTaken);
            leaderboardRepository.save(userPhaseLeaderboard);
        } 
 
        int correctPercentage = userAnswerService.calculateCorrectAnswerPercentage();

        System.out.println("user's current quizset >>>>>>>>>>.." + user.getCurrentQuizSet());
        System.out.println("quiz id::::::::" + quizId);
        if(correctPercentage >= 50 && user.getCurrentQuizSet() == quizId){
            user.setCurrentQuizSet(user.getCurrentQuizSet() + 1);
            userRepository.save(user);
        }

        if (quizAttempts.size() == 1) {

            userPhaseLeaderboard.setPoint(currentPoints + lastAttemptPoints);
            userPhaseLeaderboard.setTimeTaken(currentTimeTaken + lastAttemptTimeTaken);
            leaderboardRepository.save(userPhaseLeaderboard);

            user.setQuizSet(user.getQuizSet() + 1); // Update user quiz set count
            user.setUserXp(user.getUserXp() + 200 + bonusXp); // Add 200 XP for completing a quiz

            userRepository.save(user);
        }

        //Update user's highest score
        userPerformanceService.updateHighestScore(user);
        System.out.println("before update Total time spent::::::::::::::::::::>>>>>>> " + user.getTimeSpent());

        userPerformanceService.updateTimeSpent(user);
        System.out.println("after Total time spent::::::::::::::::::::>>>>>>> " + user.getTimeSpent());

        userPerformanceService.updateBonusXp(user, quiz);
        userPerformanceService.grantXpForPhaseCompletion(user, quiz.getTopic().getPhase());

        return ResponseEntity.ok(Map.of(
            "points", totalPoints,
            "timeTaken", timeTaken,
            "quizAttemptId", quizAttempt.getQuizAttemptId()
        ));
    }

    @GetMapping("/quiz/clear-session")
    @ResponseBody
    public ResponseEntity<?> clearSession(HttpSession session) {
        if (session == null){
            return ResponseEntity.ok(Map.of("message", "No active session found."));
        }

        if (session.getAttribute("selectedQuestions") != null ||
            session.getAttribute("currentQuestionIndex") != null ||
            session.getAttribute("userAnsers") != null ||
            session.getAttribute("startTime") != null){

                
            session.removeAttribute("selectedQuestions");
            session.removeAttribute("currentQuestionIndex");
            session.removeAttribute("userAnswers");
            session.removeAttribute("startTime");

            return ResponseEntity.ok(Map.of("message", "Session cleared"));
        }

        return ResponseEntity.ok(Map.of("message", "Session was already empty"));
    }

}
