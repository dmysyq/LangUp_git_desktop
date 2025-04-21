package com.example.langup.domain.model;

import java.util.List;

public class ContentData {
    private String version;
    private String lastUpdated;
    private List<Series> series;

    public ContentData() {
        // Required empty constructor for Gson
    }

    public ContentData(String version, String lastUpdated, List<Series> series) {
        this.version = version;
        this.lastUpdated = lastUpdated;
        this.series = series;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }

    public static class Series {
        private String id;
        private String title;
        private String description;
        private String level;
        private List<Episode> episodes;

        public Series(String id, String title, String description, String level, List<Episode> episodes) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.level = level;
            this.episodes = episodes;
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getLevel() { return level; }
        public List<Episode> getEpisodes() { return episodes; }
    }

    public static class Episode {
        private String id;
        private String title;
        private String youtubeUrl;
        private List<Question> questions;

        public Episode(String id, String title, String youtubeUrl, List<Question> questions) {
            this.id = id;
            this.title = title;
            this.youtubeUrl = youtubeUrl;
            this.questions = questions;
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getYoutubeUrl() { return youtubeUrl; }
        public List<Question> getQuestions() { return questions; }
    }

    public static class Question {
        private String id;
        private String type;
        private String question;
        private List<String> options;
        private List<String> correctAnswers;
        private boolean isSingleChoice;

        public Question(String id, String type, String question, List<String> options, List<String> correctAnswers) {
            this.id = id;
            this.type = type;
            this.question = question;
            this.options = options;
            this.correctAnswers = correctAnswers;
            this.isSingleChoice = "single_choice".equals(type);
        }

        public String getId() { return id; }
        public String getType() { return type; }
        public String getQuestion() { return question; }
        public List<String> getOptions() { return options; }
        public List<String> getCorrectAnswers() { return correctAnswers; }
        public boolean isSingleChoice() { return isSingleChoice; }
    }
} 