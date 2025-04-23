package com.example.langup.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;

public class AuthManager {
    private static final String PREFS_NAME = "LangUpPrefs";
    private static final String KEY_IS_USER_LOGGED_IN = "isUserLoggedIn";
    private static final String KEY_LAST_LOGIN_TIMESTAMP = "lastLoginTimestamp";
    private static final String KEY_LOGIN_ATTEMPTS = "loginAttempts";
    private static final String KEY_TOKEN_REFRESH_TIME = "tokenRefreshTime";
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final long LOCKOUT_DURATION = 15 * 60 * 1000; // 15 minutes in milliseconds
    private static final long TOKEN_REFRESH_INTERVAL = 30 * 60 * 1000; // 30 minutes in milliseconds

    private final SharedPreferences sharedPreferences;
    private final FirebaseAuth firebaseAuth;
    private TokenRefreshListener tokenRefreshListener;

    public interface TokenRefreshListener {
        void onTokenRefreshed(boolean success);
    }

    public AuthManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void setTokenRefreshListener(TokenRefreshListener listener) {
        this.tokenRefreshListener = listener;
    }

    public boolean isLoggedIn() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            setLoggedIn(false);
            return false;
        }
        
        // Check if token needs refresh
        long lastRefreshTime = sharedPreferences.getLong(KEY_TOKEN_REFRESH_TIME, 0);
        if (System.currentTimeMillis() - lastRefreshTime > TOKEN_REFRESH_INTERVAL) {
            refreshToken(currentUser);
        }
            
        return sharedPreferences.getBoolean(KEY_IS_USER_LOGGED_IN, false);
    }

    private void refreshToken(FirebaseUser user) {
        user.getIdToken(true)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(KEY_TOKEN_REFRESH_TIME, System.currentTimeMillis());
                    editor.apply();
                    if (tokenRefreshListener != null) {
                        tokenRefreshListener.onTokenRefreshed(true);
                    }
                } else {
                    setLoggedIn(false);
                    if (tokenRefreshListener != null) {
                        tokenRefreshListener.onTokenRefreshed(false);
                    }
                }
            });
    }

    public void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_USER_LOGGED_IN, isLoggedIn);
        if (isLoggedIn) {
            editor.putLong(KEY_LAST_LOGIN_TIMESTAMP, System.currentTimeMillis());
            editor.putLong(KEY_TOKEN_REFRESH_TIME, System.currentTimeMillis());
            resetLoginAttempts();
        }
        editor.apply();
    }

    public boolean isAccountLocked() {
        long lastLoginAttempt = sharedPreferences.getLong(KEY_LAST_LOGIN_TIMESTAMP, 0);
        int loginAttempts = sharedPreferences.getInt(KEY_LOGIN_ATTEMPTS, 0);
        
        if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            long timeSinceLastAttempt = System.currentTimeMillis() - lastLoginAttempt;
            if (timeSinceLastAttempt < LOCKOUT_DURATION) {
                return true;
            } else {
                resetLoginAttempts();
                return false;
            }
        }
        return false;
    }

    public void incrementLoginAttempts() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int attempts = sharedPreferences.getInt(KEY_LOGIN_ATTEMPTS, 0) + 1;
        editor.putInt(KEY_LOGIN_ATTEMPTS, attempts);
        editor.putLong(KEY_LAST_LOGIN_TIMESTAMP, System.currentTimeMillis());
        editor.apply();
    }

    private void resetLoginAttempts() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_LOGIN_ATTEMPTS, 0);
        editor.apply();
    }

    public long getRemainingLockoutTime() {
        long lastLoginAttempt = sharedPreferences.getLong(KEY_LAST_LOGIN_TIMESTAMP, 0);
        long timeSinceLastAttempt = System.currentTimeMillis() - lastLoginAttempt;
        return Math.max(0, LOCKOUT_DURATION - timeSinceLastAttempt);
    }

}