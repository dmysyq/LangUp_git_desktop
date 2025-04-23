package com.example.langup.data.local;

import android.content.Context;
import android.util.Log;

import com.example.langup.domain.model.Series;
import com.example.langup.domain.model.SeriesWrapper;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonLoader {
    private static final String TAG = "JsonLoader";
    private static final String SERIES_DIR = "series";
    private static final Gson gson = new Gson();
    private final Context context;

    public JsonLoader(Context context) {
        this.context = context;
    }

    public interface SeriesCallback {
        void onSuccess(List<Series> series);
        void onError(String error);
    }

    /**
     * Загружает отдельный файл серии по его имени
     * @param filename Имя файла серии (например, "wednesday.json")
     * @return Список серий (обычно содержит один элемент)
     */
    public List<Series> loadSeriesFromFile(String filename) {
        List<Series> series = new ArrayList<>();
        Series loadedSeries = loadSeriesFromAssets(filename);
        if (loadedSeries != null) {
            series.add(loadedSeries);
        }
        return series;
    }

    /**
     * Загружает список всех доступных серий из папки assets/
     * @param callback Callback для обработки результата
     */
    public void loadSeries(SeriesCallback callback) {
        try {
            List<Series> allSeries = new ArrayList<>();
            String[] files = context.getAssets().list(SERIES_DIR);
            
            if (files != null) {
                Log.d(TAG, "Found files in series directory: " + Arrays.toString(files));
                for (String file : files) {
                    if (file.endsWith(".json")) {
                        Log.d(TAG, "Loading file: " + file);
                        Series series = loadSeriesFromAssets(file);
                        if (series != null) {
                            allSeries.add(series);
                            Log.d(TAG, "Added series: " + series.getMetadata().getTitle());
                        }
                    }
                }
            }
            
            Log.d(TAG, "Total series loaded: " + allSeries.size());
            callback.onSuccess(allSeries);
        } catch (IOException e) {
            Log.e(TAG, "Error listing series from assets: " + e.getMessage());
            callback.onError("Error loading series: " + e.getMessage());
        }
    }

    /**
     * Загружает JSON-файл серии из assets и преобразует его в объект Series
     * @param filename Имя файла в папке assets/
     * @return Объект Series или null в случае ошибки
     */
    private Series loadSeriesFromAssets(String filename) {
        try {
            String fullPath = SERIES_DIR + "/" + filename;
            Log.d(TAG, "Loading series from: " + fullPath);
            
            InputStream is = context.getAssets().open(fullPath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            String jsonContent = sb.toString();
            Log.d(TAG, "JSON content for " + filename + ": " + jsonContent);

            try {
                SeriesWrapper wrapper = gson.fromJson(jsonContent, SeriesWrapper.class);
                if (wrapper != null && wrapper.getSeries() != null && !wrapper.getSeries().isEmpty()) {
                    Series series = wrapper.getSeries().get(0);
                    if (series != null) {
                        if (series.getMetadata() != null) {
                            Log.d(TAG, "Successfully parsed series: " + series.getMetadata().getTitle());
                        } else {
                            Log.e(TAG, "Series metadata is null for file: " + filename);
                        }
                    } else {
                        Log.e(TAG, "Failed to parse series from JSON for file: " + filename);
                    }
                    return series;
                } else {
                    Log.e(TAG, "No series found in wrapper for file: " + filename);
                    return null;
                }
            } catch (Exception e) {
                Log.e(TAG, "Error parsing JSON for file " + filename + ": " + e.getMessage());
                Log.e(TAG, "Error parsing JSON for file " + filename + ": " + Log.getStackTraceString(e));
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error loading series from assets: " + e.getMessage());
            Log.e(TAG, "Error loading series from assets: " + Log.getStackTraceString(e));
            return null;
        }
    }
} 