package com.example.langup.models;

import java.util.List;

public class Question {
    private String id;
    private String type; // "single_choice" или "multiple_choice"
    private String question;
    private List<String> options;
    private Object correctAnswer; // может быть int или List<Integer>

    public Question() {
        // Required empty constructor for Gson
    }

    public Question(String id, String type, String question, List<String> options, Object correctAnswer) {
        this.id = id;
        this.type = type;
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    // Getters
    public String getId() { return id; }
    public String getType() { return type; }
    public String getQuestion() { return question; }
    public List<String> getOptions() { return options; }
    public Object getCorrectAnswer() { return correctAnswer; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setQuestion(String question) { this.question = question; }
    public void setOptions(List<String> options) { this.options = options; }
    public void setCorrectAnswer(Object correctAnswer) { this.correctAnswer = correctAnswer; }

    // Helper methods
    public boolean isSingleChoice() {
        return "single_choice".equals(type);
    }

    public boolean isMultipleChoice() {
        return "multiple_choice".equals(type);
    }

    public boolean isCorrect(int answer) {
        if (isSingleChoice()) {
            return answer == (int) correctAnswer;
        }
        return false;
    }

    public boolean isCorrect(List<Integer> answers) {
        if (isMultipleChoice()) {
            @SuppressWarnings("unchecked")
            List<Integer> correctAnswers = (List<Integer>) correctAnswer;
            return answers.size() == correctAnswers.size() && 
                   answers.containsAll(correctAnswers);
        }
        return false;
    }
} 