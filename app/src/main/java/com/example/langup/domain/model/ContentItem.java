package com.example.langup.domain.model;

import java.util.List;
import java.util.Map;

public class ContentItem {
    private String id;
    private String original;
    private List<String> translation;
    private int difficulty;
    private int points;
    private List<Map<String, String>> hints;

    public ContentItem() {
        // Required empty constructor for Firestore
    }

    public ContentItem(String id, String original, List<String> translation, int difficulty, int points, List<Map<String, String>> hints) {
        this.id = id;
        this.original = original;
        this.translation = translation;
        this.difficulty = difficulty;
        this.points = points;
        this.hints = hints;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOriginal() { return original; }
    public void setOriginal(String original) { this.original = original; }

    public List<String> getTranslation() { return translation; }
    public void setTranslation(List<String> translation) { this.translation = translation; }

    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public List<Map<String, String>> getHints() { return hints; }
    public void setHints(List<Map<String, String>> hints) { this.hints = hints; }
} 