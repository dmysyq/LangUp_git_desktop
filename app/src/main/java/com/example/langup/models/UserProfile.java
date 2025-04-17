package com.example.langup.models;

import java.util.List;

public class UserProfile {
    private String uid;
    private String username;
    private String email;
    private String name;
    private Preferences preferences;
    private List<String> history;

    public UserProfile() {
        // Required empty constructor for Firestore
    }

    public UserProfile(String uid, String username, String email, String name) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.name = name;
    }

    // Getters and Setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Preferences getPreferences() { return preferences; }
    public void setPreferences(Preferences preferences) { this.preferences = preferences; }

    public List<String> getHistory() { return history; }
    public void setHistory(List<String> history) { this.history = history; }
} 