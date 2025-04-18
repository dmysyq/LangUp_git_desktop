package com.example.langup.models;

import java.util.List;
import java.util.Map;

public class Series {
    private SeriesMetadata metadata;
    private ScoringSystem scoring;
    private List<Episode> episodes;

    public Series() {
        // Required empty constructor for Firestore
    }

    public Series(SeriesMetadata metadata, ScoringSystem scoring, List<Episode> episodes) {
        this.metadata = metadata;
        this.scoring = scoring;
        this.episodes = episodes;
    }

    // Getters and Setters
    public SeriesMetadata getMetadata() { return metadata; }
    public void setMetadata(SeriesMetadata metadata) { this.metadata = metadata; }

    public ScoringSystem getScoring() { return scoring; }
    public void setScoring(ScoringSystem scoring) { this.scoring = scoring; }

    public List<Episode> getEpisodes() { return episodes; }
    public void setEpisodes(List<Episode> episodes) { this.episodes = episodes; }
} 