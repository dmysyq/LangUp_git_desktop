package com.example.langup.models;

import java.util.List;

public class ContentData {
    private String version;
    private String lastUpdated;
    private List<Series> series;
    private List<Level> levels;
    private List<Category> categories;

    public String getVersion() { return version; }
    public String getLastUpdated() { return lastUpdated; }
    public List<Series> getSeries() { return series; }
    public List<Level> getLevels() { return levels; }
    public List<Category> getCategories() { return categories; }

    public static class Series {
        private String id;
        private String title;
        private String description;
        private String level;
        private String language;
        private String accent;
        private List<Episode> episodes;

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getLevel() { return level; }
        public String getLanguage() { return language; }
        public String getAccent() { return accent; }
        public List<Episode> getEpisodes() { return episodes; }
    }

    public static class Episode {
        private String id;
        private String title;
        private String description;
        private String duration;
        private String youtubeUrl;
        private int difficulty;
        private List<VocabularyItem> vocabulary;
        private List<Question> questions;
        private List<GrammarPoint> grammar;
        private Transcript transcript;

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getDuration() { return duration; }
        public String getYoutubeUrl() { return youtubeUrl; }
        public int getDifficulty() { return difficulty; }
        public List<VocabularyItem> getVocabulary() { return vocabulary; }
        public List<Question> getQuestions() { return questions; }
        public List<GrammarPoint> getGrammar() { return grammar; }
        public Transcript getTranscript() { return transcript; }
    }

    public static class VocabularyItem {
        private String word;
        private String translation;
        private String example;

        public String getWord() { return word; }
        public String getTranslation() { return translation; }
        public String getExample() { return example; }
    }

    public static class Question {
        private String id;
        private String type;
        private String question;
        private List<String> options;
        private int correctAnswer;
        private List<Integer> correctAnswers;

        public String getId() { return id; }
        public String getType() { return type; }
        public String getQuestion() { return question; }
        public List<String> getOptions() { return options; }
        public int getCorrectAnswer() { return correctAnswer; }
        public List<Integer> getCorrectAnswers() { return correctAnswers; }

        public boolean isSingleChoice() {
            return "single_choice".equals(type);
        }

        public boolean isMultipleChoice() {
            return "multiple_choice".equals(type);
        }
    }

    public static class GrammarPoint {
        private String topic;
        private String explanation;
        private List<String> examples;

        public String getTopic() { return topic; }
        public String getExplanation() { return explanation; }
        public List<String> getExamples() { return examples; }
    }

    public static class Transcript {
        private String full;
        private List<TimestampedText> timestamps;

        public String getFull() { return full; }
        public List<TimestampedText> getTimestamps() { return timestamps; }
    }

    public static class TimestampedText {
        private String time;
        private String text;

        public String getTime() { return time; }
        public String getText() { return text; }
    }

    public static class Level {
        private String id;
        private String name;
        private String description;
        private int requiredScore;

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getRequiredScore() { return requiredScore; }
    }

    public static class Category {
        private String id;
        private String name;
        private String description;

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
    }
} 