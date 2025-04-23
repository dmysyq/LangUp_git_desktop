package com.example.langup.domain.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Episode {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("videoUrl")
    private String videoUrl;

    @SerializedName("transcript")
    private String transcript;

    @SerializedName("duration")
    private int duration;

    @SerializedName("order")
    private int order;

    // ... existing code ...
} 