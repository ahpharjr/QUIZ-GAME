package com.EDTECH.QUIZ.GAME.sevices;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EDTECH.QUIZ.GAME.models.Answer;
import com.EDTECH.QUIZ.GAME.models.Question;
import com.EDTECH.QUIZ.GAME.models.Quiz;
import com.EDTECH.QUIZ.GAME.repositories.AnswerRepository;
import com.EDTECH.QUIZ.GAME.repositories.QuestionRepository;

@Service
public class QuestionService {

    @Autowired 
    private QuestionRepository questionRepository;

    @Autowired 
    private AnswerRepository answerRepository;
    
    public void createQuestion(String question , String image , Quiz quiz 
            , Answer answer1 , Answer answer2 , Answer answer3 , Answer answer4) {
        Question newQuestion = new Question();
        newQuestion.setQuestion(question);
        newQuestion.setImage(image);
        newQuestion.setQuiz(quiz);
        
        // Save the question
        questionRepository.save(newQuestion);

        // Save the answer
        answer1.setQuestion(newQuestion);
        answer2.setQuestion(newQuestion);
        answer3.setQuestion(newQuestion);
        answer4.setQuestion(newQuestion);
        List<Answer> answers = new ArrayList<>();
        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        answers.add(answer4);
        newQuestion.setAnswers(answers);
        answerRepository.saveAll(answers);
        
    }
    public void deleteQuestion(Long questionId) {
        // Logic to delete a question
        Question question = questionRepository.findById(questionId).orElse(null);
        if (question != null) {
            questionRepository.delete(question);
        }
        answerRepository.deleteAllByQuestion_QuestionId(questionId);
    }

    //Still need to fix just create method no logic Yet

    public Question updateQuestion(Question question) {
        // Logic to update a question
        return question;
    }
    public Question getQuestionById(Long questionId) {
        // Logic to get a question by ID
        return new Question();
    }
    public List<Question> getAllQuestionsByQuizId(Long quizId) {
        // Logic to get all questions by quiz ID
        return new ArrayList<>();
    }
    public List<Question> getAllQuestionsByTopicId(Long topicId) {
        // Logic to get all questions by topic ID
        return new ArrayList<>();
    }
    public List<Question> getAllQuestionsByPhaseId(Long phaseId) {
        // Logic to get all questions by phase ID
        return new ArrayList<>();
    }
}
