package com.example.langup.domain.model;

import java.util.List;

public class Preferences {
    private List<String> genres;
    private List<String> countries;
    private List<String> sources;

    public Preferences() {
        // Required empty constructor for Firestore
    }

    public Preferences(List<String> genres, List<String> countries, List<String> sources) {
        this.genres = genres;
        this.countries = countries;
        this.sources = sources;
    }

    // Getters
    public List<String> getGenres() { return genres; }
    public List<String> getCountries() { return countries; }
    public List<String> getSources() { return sources; }

    // Setters
    public void setGenres(List<String> genres) { this.genres = genres; }
    public void setCountries(List<String> countries) { this.countries = countries; }
    public void setSources(List<String> sources) { this.sources = sources; }
} 