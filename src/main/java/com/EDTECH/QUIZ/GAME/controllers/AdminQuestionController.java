package com.EDTECH.QUIZ.GAME.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EDTECH.QUIZ.GAME.sevices.*;

@RestController
@RequestMapping("/admin/question")
public class AdminQuestionController {
    
    @Autowired
    private QuestionService questionService;

    @Autowired
    private TopicService topicService;

    

}

