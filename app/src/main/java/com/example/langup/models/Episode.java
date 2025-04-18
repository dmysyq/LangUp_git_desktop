package com.example.langup.models;

import java.util.List;

public class Episode {
    private String id;
    private String title;
    private String youtubeUrl;
    private List<Question> questions;
    private List<Content> content;

    public Episode() {
        // Required empty constructor for Gson
    }

    public Episode(String id, String title, String youtubeUrl, List<Question> questions, List<Content> content) {
        this.id = id;
        this.title = title;
        this.youtubeUrl = youtubeUrl;
        this.questions = questions;
        this.content = content;
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

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }
} 
