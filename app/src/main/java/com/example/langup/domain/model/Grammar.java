package com.example.langup.domain.model;

import java.util.List;

public class Grammar {
    private String topic;
    private String explanation;
    private List<GrammarExercise> exercises;

    public Grammar() {
        // Required empty constructor for Gson
    }

    public Grammar(String topic, String explanation, List<GrammarExercise> exercises) {
        this.topic = topic;
        this.explanation = explanation;
        this.exercises = exercises;
    }

    // Getters
    public String getTopic() { return topic; }
    public String getExplanation() { return explanation; }
    public List<GrammarExercise> getExercises() { return exercises; }

    // Setters
    public void setTopic(String topic) { this.topic = topic; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public void setExercises(List<GrammarExercise> exercises) { this.exercises = exercises; }
} 