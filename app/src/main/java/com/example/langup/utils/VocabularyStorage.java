package com.example.langup.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VocabularyStorage {
    private static final String TAG = "VocabularyStorage";
    private static final String PREF_NAME = "vocabulary";
    private static final String KEY_WORDS = "words";

    private final SharedPreferences preferences;
    private final Gson gson;

    public VocabularyStorage(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveWords(List<VocabularyItem> words) {
        try {
            String json = gson.toJson(words);
            preferences.edit().putString(KEY_WORDS, json).apply();
        } catch (Exception e) {
            Log.e(TAG, "Error saving words: " + e.getMessage());
        }
    }

    public List<VocabularyItem> loadWords() {
        try {
            String json = preferences.getString(KEY_WORDS, null);
            if (json == null) {
                return new ArrayList<>();
            }
            Type type = new TypeToken<List<VocabularyItem>>(){}.getType();
            return gson.fromJson(json, type);
        } catch (Exception e) {
            Log.e(TAG, "Error loading words: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static class VocabularyItem {
        private String word;
        private String definition;
        private String translation;

        public VocabularyItem(String word, String definition, String translation) {
            this.word = word;
            this.definition = definition;
            this.translation = translation;
        }

        public String getWord() {
            return word;
        }

        public String getDefinition() {
            return definition;
        }

        public String getTranslation() {
            return translation;
        }
    }
} 