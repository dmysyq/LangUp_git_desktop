package com.example.langup.models;

import java.util.List;

public class QuestionsData {
    private Metadata metadata;
    private List<Question> questions;

    public Metadata getMetadata() {
        return metadata;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public static class Metadata {
        private String title;
        private String series;
        private int season;
        private int episode;
        private int level;
        private String duration;
        private String accent;
        private String description;
        private String youtubeUrl;

        public String getTitle() { return title; }
        public String getSeries() { return series; }
        public int getSeason() { return season; }
        public int getEpisode() { return episode; }
        public int getLevel() { return level; }
        public String getDuration() { return duration; }
        public String getAccent() { return accent; }
        public String getDescription() { return description; }
        public String getYoutubeUrl() { return youtubeUrl; }
    }

    public static class Question {
        private String id;
        private String type;
        private String question;
        private List<String> options;
        private int correctAnswer; // для single_choice
        private List<Integer> correctAnswers; // для multiple_choice

        public Question() {}

        public Question(ContentData.Question contentQuestion) {
            this.id = contentQuestion.getId();
            this.type = contentQuestion.getType();
            this.question = contentQuestion.getQuestion();
            this.options = contentQuestion.getOptions();
            this.correctAnswer = contentQuestion.getCorrectAnswer();
            this.correctAnswers = contentQuestion.getCorrectAnswers();
        }

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
} 