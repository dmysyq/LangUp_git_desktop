package com.example.langup.models;

import java.util.List;
import java.util.Map;

public class SeriesMetadata {
    private String id;
    private Map<String, String> title;
    private Map<String, String> description;
    private List<String> genres;
    private List<String> countries;
    private String franchise;
    private int difficulty;
    private double rating;
    private int completions;
    private int releaseYear;
    private int duration;
    private String thumbnailUrl;
    private List<String> tags;

    public SeriesMetadata() {
        // Required empty constructor for Firestore
    }

    public SeriesMetadata(String id, Map<String, String> title, Map<String, String> description,
                         List<String> genres, List<String> countries, String franchise,
                         int difficulty, double rating, int completions, int releaseYear,
                         int duration, String thumbnailUrl, List<String> tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.countries = countries;
        this.franchise = franchise;
        this.difficulty = difficulty;
        this.rating = rating;
        this.completions = completions;
        this.releaseYear = releaseYear;
        this.duration = duration;
        this.thumbnailUrl = thumbnailUrl;
        this.tags = tags;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Map<String, String> getTitle() { return title; }
    public void setTitle(Map<String, String> title) { this.title = title; }

    public Map<String, String> getDescription() { return description; }
    public void setDescription(Map<String, String> description) { this.description = description; }

    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }

    public List<String> getCountries() { return countries; }
    public void setCountries(List<String> countries) { this.countries = countries; }

    public String getFranchise() { return franchise; }
    public void setFranchise(String franchise) { this.franchise = franchise; }

    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getCompletions() { return completions; }
    public void setCompletions(int completions) { this.completions = completions; }

    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
} 