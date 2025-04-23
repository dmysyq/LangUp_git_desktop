package com.example.langup.domain.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class SeriesContent implements Serializable {
    @SerializedName("episodes")
    private List<Episode> episodes;
    private List<VocabularyItem> vocabulary;
    private List<Question> questions;
    private List<Grammar> grammar;
    private Transcript transcript;

    public SeriesContent() {
        vocabulary = new ArrayList<>();
        questions = new ArrayList<>();
        grammar = new ArrayList<>();
    }

    // Getters
    public List<VocabularyItem> getVocabulary() { return vocabulary; }
    public List<Question> getQuestions() { return questions; }
    public List<Grammar> getGrammar() { return grammar; }
    public Transcript getTranscript() { return transcript; }

    // Setters
    public void setVocabulary(List<VocabularyItem> vocabulary) { this.vocabulary = vocabulary; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
    public void setGrammar(List<Grammar> grammar) { this.grammar = grammar; }
    public void setTranscript(Transcript transcript) { this.transcript = transcript; }

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

    public static class Question implements Serializable {
        private String id;
        private String type;
        private String question;
        private List<String> options;
        private List<Integer> correctAnswers;
        private Integer correctAnswer;

        public Question() {
            options = new ArrayList<>();
            correctAnswers = new ArrayList<>();
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }

        public List<String> getOptions() { return options; }
        public void setOptions(List<String> options) { this.options = options; }

        public List<Integer> getCorrectAnswers() { return correctAnswers; }
        public void setCorrectAnswers(List<Integer> correctAnswers) { this.correctAnswers = correctAnswers; }

        public Integer getCorrectAnswer() { return correctAnswer; }
        public void setCorrectAnswer(Integer correctAnswer) { this.correctAnswer = correctAnswer; }

        public boolean isSingleChoice() {
            return "single_choice".equals(type);
        }

        public boolean isMultipleChoice() {
            return "multiple_choice".equals(type);
        }
    }

    public static class Grammar implements Serializable {
        private String topic;
        private String explanation;
        private List<GrammarExercise> exercises;

        public Grammar() {
            exercises = new ArrayList<>();
        }

        public String getTopic() { return topic; }
        public void setTopic(String topic) { this.topic = topic; }

        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }

        public List<GrammarExercise> getExercises() { return exercises; }
        public void setExercises(List<GrammarExercise> exercises) { this.exercises = exercises; }

        public static class GrammarExercise implements Serializable {
            private String id;
            private String sentence;
            private List<String> options;
            private String correctAnswer;
            private List<String> correctAnswers;

            public GrammarExercise() {
                options = new ArrayList<>();
                correctAnswers = new ArrayList<>();
            }

            public String getId() { return id; }
            public void setId(String id) { this.id = id; }

            public String getSentence() { return sentence; }
            public void setSentence(String sentence) { this.sentence = sentence; }

            public List<String> getOptions() { return options; }
            public void setOptions(List<String> options) { this.options = options; }

            public String getCorrectAnswer() { return correctAnswer; }
            public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

            public List<String> getCorrectAnswers() { return correctAnswers; }
            public void setCorrectAnswers(List<String> correctAnswers) { this.correctAnswers = correctAnswers; }
        }
    }

    public static class Transcript implements Serializable {
        private String full;

        public String getFull() { return full; }
        public void setFull(String full) { this.full = full; }
    }
} 