package com.example.langup.utils;

import android.content.Context;
import com.example.langup.models.ContentData;
import com.example.langup.models.ContentData.Episode;
import com.example.langup.models.ContentData.Series;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContentManager {
    private static final String CONTENT_FILE = "content.json";
    private final Context context;
    private final Gson gson;
    private ContentData contentData;

    public ContentManager(Context context) {
        this.context = context;
        this.gson = new Gson();
        loadContent();
    }

    private void loadContent() {
        try {
            InputStream is = context.getAssets().open(CONTENT_FILE);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            contentData = gson.fromJson(json, ContentData.class);
        } catch (IOException e) {
            e.printStackTrace();
            contentData = null;
        }
    }

    public List<Series> getAllSeries() {
        return contentData != null ? contentData.getSeries() : new ArrayList<>();
    }

    public List<Series> getSeriesByLevel(String level) {
        if (contentData == null) return new ArrayList<>();
        
        List<Series> filteredSeries = new ArrayList<>();
        for (Series series : contentData.getSeries()) {
            if (level.equals(series.getLevel())) {
                filteredSeries.add(series);
            }
        }
        return filteredSeries;
    }

    public Optional<Episode> getEpisodeById(String episodeId) {
        if (contentData == null) return Optional.empty();
        
        for (Series series : contentData.getSeries()) {
            for (Episode episode : series.getEpisodes()) {
                if (episodeId.equals(episode.getId())) {
                    return Optional.of(episode);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Series> getSeriesById(String seriesId) {
        if (contentData == null) return Optional.empty();
        
        for (Series series : contentData.getSeries()) {
            if (seriesId.equals(series.getId())) {
                return Optional.of(series);
            }
        }
        return Optional.empty();
    }

    public String getVersion() {
        return contentData != null ? contentData.getVersion() : "";
    }

    public String getLastUpdated() {
        return contentData != null ? contentData.getLastUpdated() : "";
    }
} 