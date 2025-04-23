package com.example.langup.domain.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Series implements Serializable {
    @SerializedName("metadata")
    private SeriesMetadata metadata;

    @SerializedName("content")
    private SeriesContent content;

    public Series() {
        metadata = new SeriesMetadata();
        content = new SeriesContent();
    }

    // Getters
    public SeriesMetadata getMetadata() { return metadata; }
    public SeriesContent getContent() { return content; }

    // Setters
    public void setMetadata(SeriesMetadata metadata) { this.metadata = metadata; }
    public void setContent(SeriesContent content) { this.content = content; }

    // Convenience methods for metadata
    public String getId() { return metadata.getId(); }
    public String getTitle() { return metadata.getTitle(); }
    public String getDescription() { return metadata.getDescription(); }
    public String getCountry() { return metadata.getCountry(); }
    public String getAccent() { return metadata.getAccent(); }
    public String getImageUrl() { return metadata.getImageUrl(); }
    public String getSource() { return metadata.getSource(); }
    public int getDifficulty() { return metadata.getDifficulty(); }
    public List<String> getGenres() { return metadata.getGenres(); }
} 