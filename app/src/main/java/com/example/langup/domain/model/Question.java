package com.example.langup.domain.model;

import java.util.List;

public class Question {
    private String id;
    private String type;
    private String question;
    private List<String> options;
    private List<Integer> correctAnswers; // For multiple choice
    private Integer correctAnswer; // For single choice

    // Default constructor for Gson
    public Question() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<Integer> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Integer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Integer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public boolean isMultipleChoice() {
        return "multiple_choice".equals(type);
    }

    public boolean isSingleChoice() {
        return "single_choice".equals(type);
    }
} 