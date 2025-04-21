package com.example.langup.domain.model;

import java.util.List;

public class GrammarExercise {
    private String id;
    private String sentence;
    private List<String> options;
    private Object correctAnswer; // может быть String или List<String>

    public GrammarExercise() {
        // Required empty constructor for Gson
    }

    public GrammarExercise(String id, String sentence, List<String> options, Object correctAnswer) {
        this.id = id;
        this.sentence = sentence;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    // Getters
    public String getId() { return id; }
    public String getSentence() { return sentence; }
    public List<String> getOptions() { return options; }
    public Object getCorrectAnswer() { return correctAnswer; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setSentence(String sentence) { this.sentence = sentence; }
    public void setOptions(List<String> options) { this.options = options; }
    public void setCorrectAnswer(Object correctAnswer) { this.correctAnswer = correctAnswer; }

    // Helper methods
    public boolean isCorrect(String answer) {
        if (correctAnswer instanceof String) {
            return answer.equals(correctAnswer);
        } else if (correctAnswer instanceof List) {
            @SuppressWarnings("unchecked")
            List<String> correctAnswers = (List<String>) correctAnswer;
            return correctAnswers.contains(answer);
        }
        return false;
    }

    public boolean isCorrect(List<String> answers) {
        if (correctAnswer instanceof String) {
            return answers.size() == 1 && answers.get(0).equals(correctAnswer);
        } else if (correctAnswer instanceof List) {
            @SuppressWarnings("unchecked")
            List<String> correctAnswers = (List<String>) correctAnswer;
            return answers.size() == correctAnswers.size() && 
                   answers.containsAll(correctAnswers);
        }
        return false;
    }
} 