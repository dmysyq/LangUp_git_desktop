package com.example.langup.domain.model;

import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

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

    public boolean isCorrectAnswer(List<String> selectedAnswers) {
        if (isSingleChoice()) {
            if (selectedAnswers.size() != 1) return false;
            int selectedIndex = options.indexOf(selectedAnswers.get(0));
            return selectedIndex == (int) correctAnswer;
        } else if (isMultipleChoice()) {
            @SuppressWarnings("unchecked")
            List<Integer> correctIndices = (List<Integer>) correctAnswer;
            List<String> correctAnswers = correctIndices.stream()
                .map(options::get)
                .collect(Collectors.toList());
            return selectedAnswers.size() == correctAnswers.size() && 
                   selectedAnswers.containsAll(correctAnswers);
        }
        return false;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id != null ? id : JSONObject.NULL);
            json.put("type", type != null ? type : JSONObject.NULL);
            json.put("question", question != null ? question : JSONObject.NULL);
            
            JSONArray optionsArray = new JSONArray();
            if (options != null) {
                for (String option : options) {
                    optionsArray.put(option != null ? option : JSONObject.NULL);
                }
            }
            json.put("options", optionsArray);
            
            if (correctAnswer != null) {
                if (correctAnswer instanceof List) {
                    JSONArray correctAnswersArray = new JSONArray();
                    try {
                        @SuppressWarnings("unchecked")
                        List<Integer> correctAnswers = (List<Integer>) correctAnswer;
                        for (Integer answer : correctAnswers) {
                            correctAnswersArray.put(answer != null ? answer : JSONObject.NULL);
                        }
                        json.put("correctAnswer", correctAnswersArray);
                    } catch (ClassCastException e) {
                        throw new JSONException("Failed to cast correctAnswer to List<Integer>: " + e.getMessage());
                    }
                } else if (correctAnswer instanceof Integer) {
                    json.put("correctAnswer", (Integer) correctAnswer);
                } else {
                    throw new JSONException("Unexpected type for correctAnswer: " + correctAnswer.getClass().getName());
                }
            } else {
                json.put("correctAnswer", JSONObject.NULL);
            }
            
            return json;
        } catch (JSONException e) {
            throw new JSONException("Error converting Question to JSON: " + e.getMessage());
        }
    }

    public List<Integer> getCorrectAnswers() {
        List<Integer> correctIndices = new ArrayList<>();
        if (correctAnswer instanceof Integer) {
            correctIndices.add((Integer) correctAnswer);
        } else if (correctAnswer instanceof List) {
            correctIndices.addAll((List<Integer>) correctAnswer);
        }
        return correctIndices;
    }
} 