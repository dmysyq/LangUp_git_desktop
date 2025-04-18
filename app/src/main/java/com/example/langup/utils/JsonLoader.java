package com.example.langup.utils;

import android.content.Context;
import android.util.Log;

import com.example.langup.models.Episode;
import com.example.langup.models.Lesson;
import com.example.langup.models.Question;
import com.example.langup.models.Series;
import com.example.langup.models.VocabularyItem;
import com.example.langup.models.ContentData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JsonLoader {
    private static final String TAG = "JsonLoader";
    private static final Gson gson = new Gson();
    private final Context context;

    public JsonLoader(Context context) {
        this.context = context;
    }

    /**
     * Загружает JSON-файл из assets и преобразует его в объект Lesson
     * @param filename Имя файла в папке assets/lessons/
     * @return Объект Lesson или null в случае ошибки
     */
    public Lesson loadLessonFromAssets(String filename) {
        try {
            InputStream is = context.getAssets().open("lessons/" + filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            
            return gson.fromJson(sb.toString(), Lesson.class);
        } catch (IOException e) {
            Log.e(TAG, "Error loading lesson from assets: " + e.getMessage());
            return null;
        }
    }

    /**
     * Загружает список всех доступных уроков из папки assets/lessons/
     * @return Список объектов Lesson
     */
    public List<Lesson> loadAllLessons() {
        List<Lesson> lessons = new ArrayList<>();
        try {
            String[] files = context.getAssets().list("lessons");
            if (files != null) {
                for (String file : files) {
                    if (file.endsWith(".json")) {
                        Lesson lesson = loadLessonFromAssets(file);
                        if (lesson != null) {
                            lessons.add(lesson);
                        }
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error listing lessons from assets: " + e.getMessage());
        }
        return lessons;
    }

    /**
     * Преобразует объект Lesson в JSON-строку
     * @param lesson Объект Lesson
     * @return JSON-строка
     */
    public static String lessonToJson(Lesson lesson) {
        return gson.toJson(lesson);
    }

    public interface SeriesCallback {
        void onSuccess(List<Series> series);
        void onError(String error);
    }

    /**
     * Загружает список всех доступных серий из папки assets/series/
     * @param callback Callback для обработки результата
     */
    public void loadSeries(SeriesCallback callback) {
        try {
            List<Series> series = new ArrayList<>();
            String[] files = context.getAssets().list("series");
            if (files != null) {
                for (String file : files) {
                    if (file.endsWith(".json")) {
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
     * @param filename Имя файла в папке assets/series/
     * @return Объект Series или null в случае ошибки
     */
    private Series loadSeriesFromAssets(String filename) {
        try {
            InputStream is = context.getAssets().open("series/" + filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            
            return gson.fromJson(sb.toString(), Series.class);
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
} 