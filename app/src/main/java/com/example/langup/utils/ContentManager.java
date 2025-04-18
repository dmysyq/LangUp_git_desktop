package com.example.langup.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.example.langup.models.ContentData;
import com.example.langup.models.Episode;
import com.example.langup.models.Series;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContentManager {
    private static final String TAG = "ContentManager";
    private static final String PREF_NAME = "content_preferences";
    private static final String KEY_SELECTED_SERIES_ID = "selected_series_id";
    
    private final Context context;
    private final SharedPreferences preferences;
    private ContentData contentData;
    private String selectedSeriesId;

    public ContentManager(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.selectedSeriesId = preferences.getString(KEY_SELECTED_SERIES_ID, null);
        loadContentData();
    }

    private void loadContentData() {
        contentData = JsonLoader.loadContent(context, "series.json");
        if (contentData == null) {
            Log.e(TAG, "Failed to load content data");
        }
    }

    public List<Series> getAllSeries() {
        if (contentData == null) {
            return new ArrayList<>();
        }
        return contentData.getSeries();
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
        if (contentData == null) {
            return Optional.empty();
        }
        
        for (Series series : contentData.getSeries()) {
            for (Episode episode : series.getEpisodes()) {
                if (episode.getId().equals(episodeId)) {
                    return Optional.of(episode);
                }
            }
        }
        
        return Optional.empty();
    }

    public Optional<Series> getSeriesById(String seriesId) {
        if (contentData == null) {
            return Optional.empty();
        }
        
        for (Series series : contentData.getSeries()) {
            if (series.getId().equals(seriesId)) {
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

    public void setSelectedSeries(String seriesId) {
        this.selectedSeriesId = seriesId;
        preferences.edit().putString(KEY_SELECTED_SERIES_ID, seriesId).apply();
    }

    public Optional<Series> getSelectedSeries() {
        if (selectedSeriesId == null) {
            return Optional.empty();
        }
        return getSeriesById(selectedSeriesId);
    }
} 