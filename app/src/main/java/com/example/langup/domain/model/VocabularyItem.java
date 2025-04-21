package com.example.langup.domain.model;

public class VocabularyItem {
    private String word;
    private String translation;
    private String example;

    public VocabularyItem(String word, String translation, String example) {
        this.word = word;
        this.translation = translation;
        this.example = example;
    }

    public String getWord() {
        return word;
    }

    public String getTranslation() {
        return translation;
    }

    public String getExample() {
        return example;
    }
} 