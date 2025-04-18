package com.example.langup.models;

public class VocabularyWord {
    private String word;
    private String definition;
    private String translation;

    public VocabularyWord(String word, String definition, String translation) {
        this.word = word;
        this.definition = definition;
        this.translation = translation;
    }

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }

    public String getTranslation() {
        return translation;
    }
} 