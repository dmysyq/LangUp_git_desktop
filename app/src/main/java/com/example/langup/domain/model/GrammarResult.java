package com.example.langup.domain.model;

public class GrammarResult {
    private String question;
    private String userAnswer;
    private String correctAnswer;
    private String explanation;
    private boolean isCorrect;

    public GrammarResult(String question, String userAnswer, String correctAnswer, String explanation) {
        this.question = question;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
        this.isCorrect = userAnswer != null && userAnswer.trim().equalsIgnoreCase(correctAnswer.trim());
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
} 