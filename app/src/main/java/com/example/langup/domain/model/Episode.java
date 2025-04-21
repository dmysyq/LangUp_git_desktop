package com.example.langup.domain.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Episode implements Serializable {
    private String id;
    private String title;
    private String description;
    private String videoUrl;
    private int duration;
    private List<Question> questions;

    public Episode() {
        this.questions = new ArrayList<>();
    }

    public Episode(String id, String title, String description, String videoUrl, int duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
        this.duration = duration;
    }

    public Episode(String id, String title, String description, String videoUrl, int duration, List<Question> questions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
        this.duration = duration;
        this.questions = questions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
} 
