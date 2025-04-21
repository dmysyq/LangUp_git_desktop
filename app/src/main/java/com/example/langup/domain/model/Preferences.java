package com.example.langup.domain.model;

import java.util.List;

public class Preferences {
    private List<String> genres;
    private List<String> countries;
    private List<String> franchises;

    public Preferences() {
        // Required empty constructor for Firestore
    }

    public Preferences(List<String> genres, List<String> countries, List<String> franchises) {
        this.genres = genres;
        this.countries = countries;
        this.franchises = franchises;
    }

    // Getters and Setters
    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }

    public List<String> getCountries() { return countries; }
    public void setCountries(List<String> countries) { this.countries = countries; }

    public List<String> getFranchises() { return franchises; }
    public void setFranchises(List<String> franchises) { this.franchises = franchises; }
} 