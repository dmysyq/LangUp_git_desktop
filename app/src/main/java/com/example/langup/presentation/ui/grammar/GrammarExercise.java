package com.example.langup.presentation.ui.grammar;

import java.util.List;

public class GrammarExercise {
    private String sentence;
    private List<String> options;
    private String correctAnswer;
    private String selectedAnswer;

    public GrammarExercise(String sentence, List<String> options, String correctAnswer) {
        this.sentence = sentence;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.selectedAnswer = null;
    }

    public String getSentence() {
        return sentence;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public boolean isAnswered() {
        return selectedAnswer != null;
    }

    public boolean isCorrect() {
        return selectedAnswer != null && selectedAnswer.equals(correctAnswer);
    }
} 