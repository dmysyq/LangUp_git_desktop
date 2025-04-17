package com.example.langup.utils;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreferencesManager {
    private static final String TAG = "PreferencesManager";
    private final FirebaseFirestore db;

    public PreferencesManager() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void savePreferences(String userId, Map<String, List<String>> preferences, PreferencesCallback callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("preferences", preferences);

        db.collection("users").document(userId)
            .update(updates)
            .addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Preferences saved successfully");
                callback.onSuccess();
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error saving preferences", e);
                callback.onError(e.getMessage());
            });
    }

    public void loadPreferences(String userId, PreferencesCallback callback) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener(document -> {
                if (document.exists() && document.contains("preferences")) {
                    Map<String, List<String>> preferences = (Map<String, List<String>>) document.get("preferences");
                    callback.onPreferencesLoaded(preferences);
                } else {
                    // Если предпочтений нет, создаем пустые списки
                    Map<String, List<String>> emptyPreferences = new HashMap<>();
                    emptyPreferences.put("genres", new ArrayList<>());
                    emptyPreferences.put("countries", new ArrayList<>());
                    emptyPreferences.put("franchises", new ArrayList<>());
                    callback.onPreferencesLoaded(emptyPreferences);
                }
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error loading preferences", e);
                callback.onError(e.getMessage());
            });
    }

    public interface PreferencesCallback {
        void onSuccess();
        void onError(String error);
        void onPreferencesLoaded(Map<String, List<String>> preferences);
    }
} 