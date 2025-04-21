package com.example.langup.domain.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Series implements Serializable {
    private String id;
    private String title;
    private String description;
    private String level;
    private String accent;
    private String imageUrl;
    private String source;
    private int difficulty;
    private List<VocabularyItem> vocabulary;
    private List<Episode> episodes;
    private List<Question> questions;
    private List<Grammar> grammar;
    private List<Script> script;

    public Series() {
        vocabulary = new ArrayList<>();
        episodes = new ArrayList<>();
        questions = new ArrayList<>();
        grammar = new ArrayList<>();
        script = new ArrayList<>();
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLevel() { return level; }
    public String getAccent() { return accent; }
    public String getImageUrl() { return imageUrl; }
    public String getSource() { return source; }
    public int getDifficulty() { return difficulty; }
    public List<VocabularyItem> getVocabulary() { return vocabulary; }
    public List<Episode> getEpisodes() { return episodes; }
    public List<Question> getQuestions() { return questions; }
    public List<Grammar> getGrammar() { return grammar; }
    public List<Script> getScript() { return script; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setLevel(String level) { 
        this.level = level;
        this.difficulty = getDifficultyFromLevel(level);
    }
    public void setAccent(String accent) { this.accent = accent; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setSource(String source) { this.source = source; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }
    public void setVocabulary(List<VocabularyItem> vocabulary) { this.vocabulary = vocabulary; }
    public void setEpisodes(List<Episode> episodes) { this.episodes = episodes; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
    public void setGrammar(List<Grammar> grammar) { this.grammar = grammar; }
    public void setScript(List<Script> script) { this.script = script; }

    private int getDifficultyFromLevel(String level) {
        if (level == null) return 1;
        switch (level.toLowerCase()) {
            case "beginner": return 1;
            case "intermediate": return 2;
            case "advanced": return 3;
            default: return 1;
        }
    }

    public static class VocabularyItem implements Serializable {
        private String word;
        private String translation;
        private String example;

        public VocabularyItem() {}

        public VocabularyItem(String word, String translation, String example) {
            this.word = word;
            this.translation = translation;
            this.example = example;
        }

        public String getWord() { return word; }
        public void setWord(String word) { this.word = word; }

        public String getTranslation() { return translation; }
        public void setTranslation(String translation) { this.translation = translation; }

        public String getExample() { return example; }
        public void setExample(String example) { this.example = example; }
    }
} 