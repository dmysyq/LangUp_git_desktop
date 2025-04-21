package com.example.langup.domain.model;

import java.io.Serializable;

/**
 * Represents a script entry for a series episode
 */
public class Script implements Serializable {
    private String id;
    private String text;
    private String translation;
    private int startTime;
    private int endTime;

    public Script() {
    }

    public Script(String id, String text, String translation, int startTime, int endTime) {
        this.id = id;
        this.text = text;
        this.translation = translation;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
} 