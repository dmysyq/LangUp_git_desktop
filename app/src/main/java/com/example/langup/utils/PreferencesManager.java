package com.example.langup.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;

public class PreferencesManager {
    private static final String TAG = "PreferencesManager";
    private static final String PREFERENCES_FILE = "user_preferences";
    private static final String KEY_GENRES = "genres";
    private static final String KEY_COUNTRIES = "countries";
    private static final String KEY_FRANCHISES = "franchises";
    
    private final SharedPreferences sharedPreferences;
    private final FirebaseFirestore firestore;
    private final String userId;
    
    public interface PreferencesCallback {
        void onSuccess();
        void onError(String error);
        void onPreferencesLoaded(Map<String, List<String>> preferences);
    }
    
    public PreferencesManager(Context context, String userId) {
        this.sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        this.firestore = FirebaseFirestore.getInstance();
        this.userId = userId;
    }
    
    public void savePreferences(PreferencesCallback callback) {
        Map<String, List<String>> preferences = new HashMap<>();
        preferences.put(KEY_GENRES, getGenres());
        preferences.put(KEY_COUNTRIES, getCountries());
        preferences.put(KEY_FRANCHISES, getFranchises());
        
        firestore.collection("users").document(userId)
            .collection("preferences").document("content")
            .set(preferences, SetOptions.merge())
            .addOnSuccessListener(aVoid -> {
                callback.onSuccess();
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error saving preferences", e);
                callback.onError(e.getMessage());
            });
    }
    
    public void loadPreferences(PreferencesCallback callback) {
        firestore.collection("users").document(userId)
            .collection("preferences").document("content")
            .get()
            .addOnSuccessListener(document -> {
                if (document.exists()) {
                    Map<String, List<String>> preferences = new HashMap<>();
                    preferences.put(KEY_GENRES, getListFromDocument(document, KEY_GENRES));
                    preferences.put(KEY_COUNTRIES, getListFromDocument(document, KEY_COUNTRIES));
                    preferences.put(KEY_FRANCHISES, getListFromDocument(document, KEY_FRANCHISES));
                    callback.onPreferencesLoaded(preferences);
                } else {
                    callback.onPreferencesLoaded(new HashMap<>());
                }
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error loading preferences", e);
                callback.onError(e.getMessage());
            });
    }
    
    private List<String> getListFromDocument(DocumentSnapshot document, String key) {
        List<String> list = new ArrayList<>();
        if (document.contains(key)) {
            Object value = document.get(key);
            if (value instanceof List) {
                list.addAll((List<String>) value);
            }
        }
        return list;
    }
    
    public List<String> getGenres() {
        return getStringList(KEY_GENRES);
    }
    
    public List<String> getCountries() {
        return getStringList(KEY_COUNTRIES);
    }
    
    public List<String> getFranchises() {
        return getStringList(KEY_FRANCHISES);
    }
    
    private List<String> getStringList(String key) {
        return new ArrayList<>(sharedPreferences.getStringSet(key, new HashSet<>()));
    }
    
    public void setGenres(List<String> genres) {
        setStringList(KEY_GENRES, genres);
    }
    
    public void setCountries(List<String> countries) {
        setStringList(KEY_COUNTRIES, countries);
    }
    
    public void setFranchises(List<String> franchises) {
        setStringList(KEY_FRANCHISES, franchises);
    }
    
    private void setStringList(String key, List<String> list) {
        sharedPreferences.edit()
            .putStringSet(key, new HashSet<>(list))
            .apply();
    }
} 