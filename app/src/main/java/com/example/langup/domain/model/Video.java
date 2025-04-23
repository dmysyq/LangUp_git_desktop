package com.example.langup.domain.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Video {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("thumbnail_url")
    private String thumbnailUrl;

    @SerializedName("video_url")
    private String videoUrl;

    @SerializedName("duration")
    private int duration;

    @SerializedName("subtitles")
    private List<Subtitle> subtitles;

    @SerializedName("transcript")
    private Transcript transcript;

    // ... existing code ...
} 