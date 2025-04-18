package com.example.langup.models;

public class GrammarSentence {
    private String id;
    private String sentence;
    private String correctAnswer;
    private String userAnswer;
    private String explanation;

    public GrammarSentence(String id, String sentence, String correctAnswer, String explanation) {
        this.id = id;
        this.sentence = sentence;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
        this.userAnswer = "";
    }

    // Getters
    public String getId() { return id; }
    public String getSentence() { return sentence; }
    public String getCorrectAnswer() { return correctAnswer; }
    public String getUserAnswer() { return userAnswer; }
    public String getExplanation() { return explanation; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setSentence(String sentence) { this.sentence = sentence; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public boolean isCorrect() {
        return userAnswer != null && userAnswer.equalsIgnoreCase(correctAnswer);
    }
} 