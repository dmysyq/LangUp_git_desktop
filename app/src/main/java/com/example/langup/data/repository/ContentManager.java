package com.example.langup.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.langup.data.local.JsonLoader;
import com.example.langup.domain.model.Series;
import java.util.ArrayList;
import java.util.List;

public class ContentManager {
    private static final String TAG = "ContentManager";

    private List<Series> seriesList;
    private final JsonLoader jsonLoader;

    public ContentManager(Context context) {
        this.jsonLoader = new JsonLoader(context);
        this.seriesList = new ArrayList<>();
        loadContent();
    }

    private void loadContent() {
        try {
            seriesList = jsonLoader.loadSeriesFromFile("series/wednesday.json");
            Log.d(TAG, "Loaded " + seriesList.size() + " series");
        } catch (Exception e) {
            Log.e(TAG, "Error loading content", e);
            seriesList = new ArrayList<>();
        }
    }

    public List<Series> getAllSeries() {
        return seriesList;
    }

}