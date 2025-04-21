package com.example.langup.domain.utils;

import android.content.Context;
import android.util.Log;

import com.example.langup.data.local.JsonLoader;
import com.example.langup.data.local.PreferencesManager;
import com.example.langup.domain.model.Series;
import com.example.langup.domain.model.SeriesMetadata;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecommendationEngine {
    private static final String TAG = "RecommendationEngine";
    private final Context context;
    private final PreferencesManager preferencesManager;
    private final JsonLoader jsonLoader;
    private List<Series> allSeries;

    public RecommendationEngine(Context context) {
        this.context = context;
        String userId = FirebaseAuth.getInstance().getCurrentUser() != null ? 
                       FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
        this.preferencesManager = new PreferencesManager(context, userId);
        this.jsonLoader = new JsonLoader(context);
        loadSeries();
    }

    private void loadSeries() {
        jsonLoader.loadSeries(new JsonLoader.SeriesCallback() {
            @Override
            public void onSuccess(List<Series> series) {
                allSeries = series;
            }
            
            @Override
            public void onError(String error) {
                Log.e(TAG, "Error loading series: " + error);
            }
        });
    }

    public void getRecommendations(RecommendationCallback callback) {
        if (allSeries == null) {
            callback.onError("Series not loaded yet");
            return;
        }
        
        preferencesManager.loadPreferences(new PreferencesManager.PreferencesCallback() {
            @Override
            public void onSuccess() {
                // Not used for loading
            }
            
            @Override
            public void onError(String error) {
                callback.onError(error);
            }
            
            @Override
            public void onPreferencesLoaded(Map<String, List<String>> preferences) {
                List<Series> recommendations = filterSeriesByPreferences(preferences);
                callback.onSuccess(recommendations);
            }
        });
    }
    
    private List<Series> filterSeriesByPreferences(Map<String, List<String>> preferences) {
        List<Series> filteredSeries = new ArrayList<>();
        List<String> preferredGenres = preferences.get("genres");
        List<String> preferredCountries = preferences.get("countries");
        List<String> preferredSources = preferences.get("sources");
        
        for (Series series : allSeries) {
            boolean matchesPreferences = true;
            SeriesMetadata metadata = series.getMetadata();
            
            if (preferredGenres != null && !preferredGenres.isEmpty()) {
                matchesPreferences &= metadata.getGenres().stream()
                    .anyMatch(preferredGenres::contains);
            }
            
            if (preferredCountries != null && !preferredCountries.isEmpty()) {
                matchesPreferences &= metadata.getCountries().stream()
                    .anyMatch(preferredCountries::contains);
            }
            
            if (preferredSources != null && !preferredSources.isEmpty()) {
                matchesPreferences &= preferredSources.contains(metadata.getSource());
            }
            
            if (matchesPreferences) {
                filteredSeries.add(series);
            }
        }
        
        return filteredSeries;
    }

    public interface RecommendationCallback {
        void onSuccess(List<Series> recommendations);
        void onError(String error);
    }
} 