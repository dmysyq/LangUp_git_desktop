package com.example.langup.domain.model;

import com.google.gson.annotations.SerializedName;

public class Transcript {
    @SerializedName("id")
    private String id;

    @SerializedName("language")
    private String language;

    @SerializedName("url")
    private String url;

    // ... existing code ...
} 