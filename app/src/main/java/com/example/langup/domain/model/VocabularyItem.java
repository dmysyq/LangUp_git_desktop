package com.example.langup.domain.model;

public class VocabularyItem {
    private String example;
    private String word;
    private String translation;

    public VocabularyItem(String example, String word, String translation) {
        this.example = example;
        this.word = word;
        this.translation = translation;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
} 