package com.example.langup.data.local;

import android.content.Context;
import android.util.Log;

import com.example.langup.domain.model.Series;
import com.example.langup.domain.model.ContentData;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JsonLoader {
    private static final String TAG = "JsonLoader";
    private static final Gson gson = new Gson();
    private final Context context;

    public JsonLoader(Context context) {
        this.context = context;
    }

    public interface SeriesCallback {
        void onSuccess(List<Series> series);
        void onError(String error);
    }

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
            List<Series> series = new ArrayList<>();
            String[] files = context.getAssets().list("");
            if (files != null) {
                for (String file : files) {
                    if (file.endsWith(".json") && !file.equals("series.json") && !file.equals("content.json")) {
                        Series seriesItem = loadSeriesFromAssets(file);
                        if (seriesItem != null) {
                            series.add(seriesItem);
                        }
                    }
                }
            }
            callback.onSuccess(series);
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
            InputStream is = context.getAssets().open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            
            Series series = gson.fromJson(sb.toString(), Series.class);
            
            // Ensure difficulty and level are set correctly
            if (series != null) {
                if (series.getLevel() != null) {
                    series.setLevel(series.getLevel()); // This will update difficulty
                } else if (series.getDifficulty() > 0) {
                    series.setDifficulty(series.getDifficulty()); // This will update level
                }
            }
            
            return series;
        } catch (IOException e) {
            Log.e(TAG, "Error loading series from assets: " + e.getMessage());
            return null;
        }
    }

    public static ContentData loadContent(Context context, String filename) {
        try {
            InputStream is = context.getAssets().open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            reader.close();
            is.close();
            
            String json = sb.toString();
            return gson.fromJson(json, ContentData.class);
        } catch (IOException e) {
            Log.e(TAG, "Error loading JSON file: " + filename, e);
            return null;
        }
    }

    /**
     * Загружает отдельный файл серии по его имени
     * @param filename Имя файла серии (например, "wednesday_s1e07.json")
     * @return Объект Series или null в случае ошибки
     */
    public static Series loadSeriesFile(Context context, String filename) {
        try {
            InputStream is = context.getAssets().open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            reader.close();
            is.close();
            
            String json = sb.toString();
            Series series = gson.fromJson(json, Series.class);
            
            // Ensure difficulty and level are set correctly
            if (series != null) {
                if (series.getLevel() != null) {
                    series.setLevel(series.getLevel()); // This will update difficulty
                } else if (series.getDifficulty() > 0) {
                    series.setDifficulty(series.getDifficulty()); // This will update level
                }
            }
            
            return series;
        } catch (IOException e) {
            Log.e(TAG, "Error loading series file: " + filename, e);
            return null;
        }
    }
} 