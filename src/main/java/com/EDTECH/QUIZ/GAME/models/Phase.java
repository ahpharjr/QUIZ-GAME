package com.EDTECH.QUIZ.GAME.models;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table (name = "phases")
public class Phase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long phaseId;
    
    private String name;
    private String image;
    private String desc;

    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Topic> topics;

    public List<Topic> getTopics() {
        return topics;
    }
    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public Long getPhaseId() {
        return phaseId;
    }
    public void setPhaseId(Long phaseId) {
        this.phaseId = phaseId;
    }

    
}
