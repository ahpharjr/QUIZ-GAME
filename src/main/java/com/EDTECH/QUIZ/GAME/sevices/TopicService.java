package com.EDTECH.QUIZ.GAME.sevices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EDTECH.QUIZ.GAME.models.Topic;
import com.EDTECH.QUIZ.GAME.repositories.TopicRepository;

@Service
public class TopicService {
    
    @Autowired
    private TopicRepository topicRepository;

    public Topic createTopic(Topic topic) {
        return topicRepository.save(topic);
    }
    public void deleteTopic(long topicId) {
        topicRepository.deleteById(topicId);
    }
    public Topic updateTopic(Topic topic) {
        return topicRepository.save(topic);
    }
    public Topic getTopicById(long topicId) {
        return topicRepository.findById(topicId).orElse(null);
    }
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }
    public List<Topic> getTopicsByPhase(long phaseId) {
        return topicRepository.findAllByPhasePhaseId(phaseId);
    }
    
}
