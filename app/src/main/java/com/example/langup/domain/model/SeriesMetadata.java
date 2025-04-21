package com.example.langup.domain.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class SeriesMetadata implements Serializable {
    private List<String> genres;
    private String country;
    private List<String> franchises;
    private String releaseYear;
    private String rating;
    private List<String> languages;

    public SeriesMetadata() {
        this.genres = new ArrayList<>();
        this.franchises = new ArrayList<>();
        this.languages = new ArrayList<>();
    }

    // Getters
    public List<String> getGenres() { return genres; }
    public String getCountry() { return country; }
    public List<String> getFranchises() { return franchises; }
    public String getReleaseYear() { return releaseYear; }
    public String getRating() { return rating; }
    public List<String> getLanguages() { return languages; }

    // Setters
    public void setGenres(List<String> genres) { this.genres = genres; }
    public void setCountry(String country) { this.country = country; }
    public void setFranchises(List<String> franchises) { this.franchises = franchises; }
    public void setReleaseYear(String releaseYear) { this.releaseYear = releaseYear; }
    public void setRating(String rating) { this.rating = rating; }
    public void setLanguages(List<String> languages) { this.languages = languages; }
} 