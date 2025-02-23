package com.EDTECH.QUIZ.GAME.sevices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EDTECH.QUIZ.GAME.models.Quiz;
import com.EDTECH.QUIZ.GAME.models.QuizAttempt;
import com.EDTECH.QUIZ.GAME.models.UserAnswer;
import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.repositories.QuizAttemptRepository;
import com.EDTECH.QUIZ.GAME.repositories.UserAnswerRepository;

@Service
public class QuizAttemptService {

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    public QuizAttempt saveQuizAttempt(Users user, Quiz quiz, int totalPoints, long timeTaken, List<UserAnswer> userAnswers, int bonusXp) {
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setUser(user);
        quizAttempt.setQuiz(quiz);
        quizAttempt.setTotalPoints(totalPoints);
        quizAttempt.setTotalTime(timeTaken);
        quizAttempt.setBonusXp(bonusXp);

        // Save QuizAttempt first so that UserAnswers can reference it
        quizAttempt = quizAttemptRepository.save(quizAttempt);

        // Associate userAnswers with the quiz attempt
        for (UserAnswer answer : userAnswers) {
            answer.setQuizAttempt(quizAttempt);
            userAnswerRepository.save(answer);
        }

        return quizAttempt;
    }
}
