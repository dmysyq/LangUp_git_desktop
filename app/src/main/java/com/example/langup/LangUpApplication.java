package com.example.langup;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.example.langup.utils.LocaleManager;

public class LangUpApplication extends Application {
    private LocaleManager localeManager;

    @Override
    protected void attachBaseContext(Context base) {
        // Create a temporary LocaleManager just for context wrapping
        // We'll create the permanent instance in onCreate
        super.attachBaseContext(LocaleManager.onAttach(base));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        
        // Initialize the LocaleManager with the application context
        localeManager = LocaleManager.getInstance(this);
        
        // Apply the current locale
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(localeManager.getCurrentLocale());
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }

    public LocaleManager getLocaleManager() {
        return localeManager;
    }
} 