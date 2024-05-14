package com.example.langup;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthManager {
    private static final String PREFS_NAME = "AppNamePrefs";
    private static final String KEY_IS_USER_LOGGED_IN = "isUserLoggedIn";

    private final SharedPreferences sharedPreferences;

    public AuthManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_USER_LOGGED_IN, false);
    }

    public void setUserLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_USER_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

}
