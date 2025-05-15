package com.example.langup.presentation.model;

public class LanguageItem {
    private String code;
    private int drawableResId;

    public LanguageItem(String code, int drawableResId) {
        this.code = code;
        this.drawableResId = drawableResId;
    }

    public String getCode() {
        return code;
    }

    public int getDrawableResId() {
        return drawableResId;
    }
} 