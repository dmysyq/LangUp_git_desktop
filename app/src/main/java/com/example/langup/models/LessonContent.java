package com.example.langup.models;

import java.util.List;
import java.util.Map;

public class LessonContent {
    private Metadata metadata;
    private Content content;
    private List<Exercise> exercises;

    public static class Metadata {
        private String id;
        private String title;
        private String description;
        private int level;
        private String category;
        private Map<String, String> translations;

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public int getLevel() { return level; }
        public void setLevel(int level) { this.level = level; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public Map<String, String> getTranslations() { return translations; }
        public void setTranslations(Map<String, String> translations) { this.translations = translations; }
    }

    public static class Content {
        private String audioUrl;
        private String transcript;
        private List<String> questions;
        private List<String> grammarPoints;
        private List<String> vocabulary;
        private Map<String, String> translations;

        // Getters and setters
        public String getAudioUrl() { return audioUrl; }
        public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }
        public String getTranscript() { return transcript; }
        public void setTranscript(String transcript) { this.transcript = transcript; }
        public List<String> getQuestions() { return questions; }
        public void setQuestions(List<String> questions) { this.questions = questions; }
        public List<String> getGrammarPoints() { return grammarPoints; }
        public void setGrammarPoints(List<String> grammarPoints) { this.grammarPoints = grammarPoints; }
        public List<String> getVocabulary() { return vocabulary; }
        public void setVocabulary(List<String> vocabulary) { this.vocabulary = vocabulary; }
        public Map<String, String> getTranslations() { return translations; }
        public void setTranslations(Map<String, String> translations) { this.translations = translations; }
    }

    public static class Exercise {
        private String type; // "questions", "grammar", "vocabulary"
        private String question;
        private List<String> options;
        private String correctAnswer;
        private Map<String, String> translations;

        // Getters and setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }
        public List<String> getOptions() { return options; }
        public void setOptions(List<String> options) { this.options = options; }
        public String getCorrectAnswer() { return correctAnswer; }
        public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
        public Map<String, String> getTranslations() { return translations; }
        public void setTranslations(Map<String, String> translations) { this.translations = translations; }
    }

    // Getters and setters for the main class
    public Metadata getMetadata() { return metadata; }
    public void setMetadata(Metadata metadata) { this.metadata = metadata; }
    public Content getContent() { return content; }
    public void setContent(Content content) { this.content = content; }
    public List<Exercise> getExercises() { return exercises; }
    public void setExercises(List<Exercise> exercises) { this.exercises = exercises; }
} 