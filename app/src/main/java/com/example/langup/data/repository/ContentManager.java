package com.example.langup.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.langup.data.local.JsonLoader;
import com.example.langup.domain.model.Episode;
import com.example.langup.domain.model.Series;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContentManager {
    private static final String TAG = "ContentManager";
    private static final String PREF_NAME = "content_preferences";
    private static final String KEY_SELECTED_SERIES_ID = "selected_series_id";
    
    private final Context context;
    private final SharedPreferences preferences;
    private List<Series> seriesList;
    private String selectedSeriesId;
    private final JsonLoader jsonLoader;

    public ContentManager(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.selectedSeriesId = preferences.getString(KEY_SELECTED_SERIES_ID, null);
        this.jsonLoader = new JsonLoader(context);
        this.seriesList = new ArrayList<>();
        loadContent();
    }

    private void loadContent() {
        try {
            seriesList = jsonLoader.loadSeriesFromFile("wednesday.json");
            Log.d(TAG, "Loaded " + seriesList.size() + " series");
        } catch (Exception e) {
            Log.e(TAG, "Error loading content", e);
            seriesList = new ArrayList<>();
        }
    }

    public List<Series> getAllSeries() {
        return seriesList;
    }

    public List<Series> getSeriesByLevel(String level) {
        List<Series> filteredSeries = new ArrayList<>();
        for (Series series : seriesList) {
            if (series.getLevel() != null && series.getLevel().equals(level)) {
                filteredSeries.add(series);
            }
        }
        return filteredSeries;
    }

    public Optional<Episode> getEpisodeById(String seriesId, String episodeId) {
        Series series = getSeriesById(seriesId);
        if (series != null && series.getEpisodes() != null) {
            for (Episode episode : series.getEpisodes()) {
                if (episode.getId().equals(episodeId)) {
                    return Optional.of(episode);
                }
            }
        }
        return Optional.empty();
    }

    public Series getSeriesById(String id) {
        if (id == null) return null;
        for (Series series : seriesList) {
            if (series.getId().equals(id)) {
                return series;
            }
        }
        return null;
    }

    public void setSelectedSeries(String seriesId) {
        this.selectedSeriesId = seriesId;
        preferences.edit().putString(KEY_SELECTED_SERIES_ID, seriesId).apply();
    }

    public Series getSelectedSeries() {
        return selectedSeriesId != null ? getSeriesById(selectedSeriesId) : null;
    }

    public Series loadSeriesFile(String filename) {
        try {
            return jsonLoader.loadSeriesFromFile(filename).get(0);
        } catch (Exception e) {
            Log.e(TAG, "Error loading series file: " + filename, e);
            return null;
        }
    }
} 