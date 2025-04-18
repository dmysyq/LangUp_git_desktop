package com.example.langup.models;

import java.util.List;

public class SeriesMetadata {
    private String country;
    private List<String> genres;
    private List<String> franchises;

    public SeriesMetadata() {
        // Required empty constructor for Gson
    }

    public SeriesMetadata(String country, List<String> genres, List<String> franchises) {
        this.country = country;
        this.genres = genres;
        this.franchises = franchises;
    }

    // Getters
    public String getCountry() { return country; }
    public List<String> getGenres() { return genres; }
    public List<String> getFranchises() { return franchises; }

    // Setters
    public void setCountry(String country) { this.country = country; }
    public void setGenres(List<String> genres) { this.genres = genres; }
    public void setFranchises(List<String> franchises) { this.franchises = franchises; }
} 