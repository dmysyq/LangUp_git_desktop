package com.example.langup.domain.model;

import java.util.List;

public class QuestionsData {
    private List<Question> questions;

    public QuestionsData(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public static class Question {
        private String question;
        private List<String> options;
        private List<String> correctAnswers;
        private boolean isSingleChoice;

        public Question(String question, List<String> options, List<String> correctAnswers, boolean isSingleChoice) {
            this.question = question;
            this.options = options;
            this.correctAnswers = correctAnswers;
            this.isSingleChoice = isSingleChoice;
        }

        public String getQuestion() {
            return question;
        }

        public List<String> getOptions() {
            return options;
        }

        public List<String> getCorrectAnswers() {
            return correctAnswers;
        }

        public boolean isSingleChoice() {
            return isSingleChoice;
        }

        public boolean isCorrect(List<String> selectedAnswers) {
            if (selectedAnswers == null || selectedAnswers.isEmpty()) {
                return false;
            }

            if (isSingleChoice) {
                return selectedAnswers.size() == 1 && 
                       correctAnswers.contains(selectedAnswers.get(0));
            } else {
                if (selectedAnswers.size() != correctAnswers.size()) {
                    return false;
                }
                return selectedAnswers.containsAll(correctAnswers);
            }
        }
    }
} 