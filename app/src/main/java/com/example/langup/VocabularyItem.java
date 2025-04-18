package com.example.langup;

public class VocabularyItem {
    private String word;
    private String description;
    private String translation;

    public VocabularyItem(String word, String description, String translation) {
        this.word = word;
        this.description = description;
        this.translation = translation;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
} 