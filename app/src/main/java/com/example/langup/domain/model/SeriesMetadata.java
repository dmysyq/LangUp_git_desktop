package com.example.langup.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SeriesMetadata implements Serializable {
    private String id;
    private String title;
    private String description;
    private String country;
    private String accent;
    private String imageUrl;
    private String source;
    private int difficulty;
    private List<String> genres;
    private List<String> countries;

    public SeriesMetadata() {
        genres = new ArrayList<>();
        countries = new ArrayList<>();
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCountry() { return country; }
    public String getAccent() { return accent; }
    public String getImageUrl() { return imageUrl; }
    public String getSource() { return source; }
    public int getDifficulty() { return difficulty; }
    public List<String> getGenres() { return genres; }
    public List<String> getCountries() { return countries; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCountry(String country) { this.country = country; }
    public void setAccent(String accent) { this.accent = accent; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setSource(String source) { this.source = source; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }
    public void setGenres(List<String> genres) { this.genres = genres; }
    public void setCountries(List<String> countries) { this.countries = countries; }
} 