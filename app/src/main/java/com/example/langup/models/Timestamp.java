package com.example.langup.models;

public class Timestamp {
    private String time;
    private String text;

    public Timestamp() {
        // Required empty constructor for Gson
    }

    public Timestamp(String time, String text) {
        this.time = time;
        this.text = text;
    }

    // Getters
    public String getTime() { return time; }
    public String getText() { return text; }

    // Setters
    public void setTime(String time) { this.time = time; }
    public void setText(String text) { this.text = text; }
} 