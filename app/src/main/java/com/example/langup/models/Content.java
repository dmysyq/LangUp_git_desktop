package com.example.langup.models;

import java.util.List;

public class Content {
    private String original;
    private List<String> translation;

    public Content() {
        // Required empty constructor for Gson
    }

    public Content(String original, List<String> translation) {
        this.original = original;
        this.translation = translation;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }
} 