package com.EDTECH.QUIZ.GAME.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.EDTECH.QUIZ.GAME.models.Topic;
import com.EDTECH.QUIZ.GAME.repositories.PhaseRepository;
import com.EDTECH.QUIZ.GAME.repositories.TopicRepository;
import com.EDTECH.QUIZ.GAME.sevices.TopicService;

@RestController
@RequestMapping("/admin/topic")
public class AdminTopicController {
    
    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TopicService topicService;

    @GetMapping("/create")
    public String createTopic(@RequestParam Long id, @RequestParam String topicName , @RequestParam String image , @RequestParam String desc , @RequestParam Long phaseId) {
        
        Topic topic = new Topic();
        topic.setName(topicName);
        topic.setDesc(desc);
        topic.setPhase(phaseRepository.findById(phaseId).orElse(null));
        String img = "/images/flashcards/topics/" + image;
        topic.setImage(img);
        topicService.createTopic(topic);
        return "Topic created successfully";
    }

    @GetMapping("/delete")
    public String deleteTopic(@RequestParam Long id) {
        topicService.deleteTopic(id);
        return "Topic deleted successfully";
    }

    @GetMapping("/update")
    public Topic updateTopic(@RequestParam Long id, @RequestParam String topicName , @RequestParam String image , @RequestParam String desc , @RequestParam Long phaseId) {
        Topic topic = topicService.getTopicById(id);
        topic.setName(topicName);
        String img = "/images/flashcards/topics/" + image;
        topic.setImage(img);
        topic.setDesc(desc);
        return topicService.updateTopic(topic);
    }
    
    @GetMapping("/get")
    public Topic getTopic(@RequestParam Long id) {
        return topicService.getTopicById(id);
    }

    @GetMapping("/getAll")
    public List<Topic> getAllTopics() {
        return topicService.getAllTopics();
    }

    @GetMapping("/getByPhase")
    public List<Topic> getTopicsByPhase(@RequestParam Long phaseId) {
        return topicService.getTopicsByPhase(phaseId);
    }

}
