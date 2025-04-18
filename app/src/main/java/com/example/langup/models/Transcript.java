package com.example.langup.models;

import java.util.List;

public class Transcript {
    private String full;
    private List<Timestamp> timestamps;

    public Transcript() {
        // Required empty constructor for Gson
    }

    public Transcript(String full, List<Timestamp> timestamps) {
        this.full = full;
        this.timestamps = timestamps;
    }

    // Getters
    public String getFull() { return full; }
    public List<Timestamp> getTimestamps() { return timestamps; }

    // Setters
    public void setFull(String full) { this.full = full; }
    public void setTimestamps(List<Timestamp> timestamps) { this.timestamps = timestamps; }
} 