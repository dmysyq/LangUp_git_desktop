package com.example.langup.models;

import java.util.List;
import java.util.Map;

public class Episode {
    private int episodeNumber;
    private Map<String, String> episodeTitle;
    private List<ContentItem> content;

    public Episode() {
        // Required empty constructor for Firestore
    }

    public Episode(int episodeNumber, Map<String, String> episodeTitle, List<ContentItem> content) {
        this.episodeNumber = episodeNumber;
        this.episodeTitle = episodeTitle;
        this.content = content;
    }

    // Getters and Setters
    public int getEpisodeNumber() { return episodeNumber; }
    public void setEpisodeNumber(int episodeNumber) { this.episodeNumber = episodeNumber; }

    public Map<String, String> getEpisodeTitle() { return episodeTitle; }
    public void setEpisodeTitle(Map<String, String> episodeTitle) { this.episodeTitle = episodeTitle; }

    public List<ContentItem> getContent() { return content; }
    public void setContent(List<ContentItem> content) { this.content = content; }
} 