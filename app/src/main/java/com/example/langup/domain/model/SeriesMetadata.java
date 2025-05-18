package com.example.langup.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SeriesMetadata implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("country")
    private String country;

    @SerializedName("accent")
    private String accent;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("videoUrl")
    private String videoUrl;

    @SerializedName("source")
    private String source;

    @SerializedName("difficulty")
    private int difficulty;

    @SerializedName("genres")
    private List<String> genres;

    @SerializedName("countries")
    private List<String> countries;

    @SerializedName("lang")
    private String lang;

    @SerializedName("isPremium")
    private boolean isPremium;

    public SeriesMetadata() {
        genres = new ArrayList<>();
        countries = new ArrayList<>();
        isPremium = false;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCountry() { return country; }
    public String getAccent() { return accent; }
    public String getImageUrl() { return imageUrl; }
    public String getVideoUrl() { return videoUrl; }
    public String getSource() { return source; }
    public int getDifficulty() { return difficulty; }
    public List<String> getGenres() { return genres; }
    public List<String> getCountries() { return countries; }
    public String getLang() { return lang; }
    public boolean isPremium() { return isPremium; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }

    public void setCountry(String country) { this.country = country; }
    public void setAccent(String accent) { this.accent = accent; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public void setSource(String source) { this.source = source; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }
    public void setGenres(List<String> genres) { this.genres = genres; }
    public void setCountries(List<String> countries) { this.countries = countries; }
    public void setLang(String lang) { this.lang = lang; }
    public void setPremium(boolean premium) { isPremium = premium; }
} 