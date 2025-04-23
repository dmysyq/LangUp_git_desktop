package com.example.langup.presentation.model;

import java.util.List;

public class GrammarExercise {
    private String sentence;
    private List<String> options;
    private List<String> correctAnswers;

    public GrammarExercise(String sentence, List<String> options, List<String> correctAnswers) {
        this.sentence = sentence;
        this.options = options;
        this.correctAnswers = correctAnswers;
    }

    public String getSentence() {
        return sentence;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }
} 