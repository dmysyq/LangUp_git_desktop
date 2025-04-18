package com.example.langup.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Series {
    private String id;
    private String title;
    private String description;
    private String country;
    private String accent;
    private String imageUrl;
    private String source;
    private int difficulty;
    private List<VocabularyItem> vocabulary;
    private List<Question> questions;
    private List<Grammar> grammar;
    private Transcript transcript;

    public Series() {
        // Required empty constructor for Gson
    }

    public Series(String id, String title, String description, String country, 
                 String accent, String imageUrl, String source, int difficulty,
                 List<VocabularyItem> vocabulary, List<Question> questions,
                 List<Grammar> grammar, Transcript transcript) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.country = country;
        this.accent = accent;
        this.imageUrl = imageUrl;
        this.source = source;
        this.difficulty = difficulty;
        this.vocabulary = vocabulary;
        this.questions = questions;
        this.grammar = grammar;
        this.transcript = transcript;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCountry() { return country; }
    public String getAccent() { return accent; }
    public String getImageUrl() { return imageUrl; }
    public String getSource() { return source; }
    public int getDifficulty() { return difficulty; }
    public List<VocabularyItem> getVocabulary() { return vocabulary; }
    public List<Question> getQuestions() { return questions; }
    public List<Grammar> getGrammar() { return grammar; }
    public Transcript getTranscript() { return transcript; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCountry(String country) { this.country = country; }
    public void setAccent(String accent) { this.accent = accent; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setSource(String source) { this.source = source; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }
    public void setVocabulary(List<VocabularyItem> vocabulary) { this.vocabulary = vocabulary; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
    public void setGrammar(List<Grammar> grammar) { this.grammar = grammar; }
    public void setTranscript(Transcript transcript) { this.transcript = transcript; }
} 