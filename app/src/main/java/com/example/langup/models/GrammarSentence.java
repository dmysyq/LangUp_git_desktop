package com.example.langup.models;

import java.util.List;

public class GrammarSentence {
    private String id;
    private List<String> parts;
    private List<String> answers;

    public GrammarSentence(String id, List<String> parts, List<String> answers) {
        this.id = id;
        this.parts = parts;
        this.answers = answers;
    }

    public String getId() {
        return id;
    }

    public List<String> getParts() {
        return parts;
    }

    public List<String> getAnswers() {
        return answers;
    }
} 