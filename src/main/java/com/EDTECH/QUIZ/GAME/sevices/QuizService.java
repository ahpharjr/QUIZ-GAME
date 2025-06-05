package com.EDTECH.QUIZ.GAME.sevices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EDTECH.QUIZ.GAME.models.Phase;
import com.EDTECH.QUIZ.GAME.models.Quiz;
import com.EDTECH.QUIZ.GAME.repositories.PhaseRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuizRepository;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    public List<Quiz> getAllQuizByPhaseId(long phaseId) {

        Phase phase = phaseRepository.findById(phaseId).orElse(null);
        
        return quizRepository.findByTopicPhase(phase);
    }

    public Quiz getQuizById(long quizId) {
        return quizRepository.findById(quizId).orElse(null);
    }

    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public void deleteQuiz(long quizId) {
        quizRepository.deleteById(quizId);
    }

    public Quiz updateQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }
    
    public Quiz getQuizIdByTopicId(long topicId) {
        return quizRepository.findQuizIdByTopic_TopicId(topicId);
    }
    
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }
    
}
