package com.example.langup.domain.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

public class LocaleManager {
    private static final String TAG = "LocaleManager";
    private static final String LANGUAGE_KEY = "language_key";
    private static final String LANGUAGE_SYSTEM = "system";
    private static final String LANGUAGE_EN = "en";
    private static final String LANGUAGE_RU = "ru";
    private static final String LANGUAGE_FR = "fr";
    private static final String LANGUAGE_ES = "es";
    private static final String LANGUAGE_KK = "kk";

    private static volatile LocaleManager instance;
    private SharedPreferences preferences;
    private Context context;

    public LocaleManager() {
        // Private constructor
    }

    public static synchronized LocaleManager getInstance(Context context) {
        if (instance == null) {
            synchronized (LocaleManager.class) {
                if (instance == null) {
                    instance = new LocaleManager();
                }
            }
        }
        instance.init(context);
        return instance;
    }

    public static Context onAttach(Context context) {
        LocaleManager manager = getInstance(context);
        manager.init(context);
        String lang = manager.getCurrentLanguage();
        return manager.setLocale(context, lang);
    }

    private void init(Context context) {
        if (context != null) {
            this.context = context.getApplicationContext() != null ? 
                          context.getApplicationContext() : 
                          context;
            if (preferences == null) {
                try {
                    this.preferences = this.context.getSharedPreferences("locale_prefs", Context.MODE_PRIVATE);
                } catch (Exception e) {
                    Log.e(TAG, "Error initializing preferences", e);
                }
            }
        }
    }

    public void setLocale(String language) {
        if (preferences == null) return;
        
        // Проверяем валидность языка
        if (!isValidLanguage(language)) {
            language = LANGUAGE_EN;
        }
        
        Log.d(TAG, "Setting locale to: " + language);
        preferences.edit().putString(LANGUAGE_KEY, language).apply();
        updateConfiguration(language);
    }

    private boolean isValidLanguage(String language) {
        return language != null && (
            language.equals(LANGUAGE_EN) ||
            language.equals(LANGUAGE_RU) ||
            language.equals(LANGUAGE_FR) ||
            language.equals(LANGUAGE_ES) ||
            language.equals(LANGUAGE_KK) ||
            language.equals(LANGUAGE_SYSTEM)
        );
    }

    private Context setLocale(Context context, String language) {
        try {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);

            Resources resources = context.getResources();
            Configuration configuration = new Configuration(resources.getConfiguration());
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(locale);
                context = context.createConfigurationContext(configuration);
            } else {
                configuration.locale = locale;
                resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            }

            return context;
        } catch (Exception e) {
            Log.e(TAG, "Error setting locale", e);
            return context;
        }
    }

    public String getCurrentLanguage() {
        if (preferences == null) return LANGUAGE_EN;
        
        try {
            String savedLanguage = preferences.getString(LANGUAGE_KEY, LANGUAGE_SYSTEM);
            if (savedLanguage.equals(LANGUAGE_SYSTEM)) {
                return getSystemLanguage();
            }
            return savedLanguage;
        } catch (Exception e) {
            Log.e(TAG, "Error getting current language", e);
            return LANGUAGE_EN;
        }
    }

    private String getSystemLanguage() {
        if (context == null) return LANGUAGE_EN;

        try {
            Locale systemLocale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                systemLocale = context.getResources().getConfiguration().getLocales().get(0);
            } else {
                systemLocale = context.getResources().getConfiguration().locale;
            }
            
            String language = systemLocale.getLanguage();
            switch (language) {
                case LANGUAGE_RU:
                    return LANGUAGE_RU;
                case LANGUAGE_FR:
                    return LANGUAGE_FR;
                case LANGUAGE_ES:
                    return LANGUAGE_ES;
                case LANGUAGE_KK:
                    return LANGUAGE_KK;
                default:
                    return LANGUAGE_EN;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting system language", e);
            return LANGUAGE_EN;
        }
    }

    private void updateConfiguration(String language) {
        if (context == null) return;

        try {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);

            Resources resources = context.getResources();
            Configuration configuration = new Configuration(resources.getConfiguration());
            DisplayMetrics metrics = resources.getDisplayMetrics();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(locale);
                context.createConfigurationContext(configuration);
            } else {
                configuration.locale = locale;
            }

            resources.updateConfiguration(configuration, metrics);
        } catch (Exception e) {
            Log.e(TAG, "Error updating configuration", e);
        }
    }

    public void applyCurrentLocale() {
        if (context == null) return;
        String currentLanguage = getCurrentLanguage();
        updateConfiguration(currentLanguage);
    }

    public Locale getCurrentLocale() {
        return new Locale(getCurrentLanguage());
    }

    public void forceLocaleUpdate(Activity activity) {
        if (activity == null) return;
        
        String currentLanguage = getCurrentLanguage();
        Log.d(TAG, "Forcing locale update to: " + currentLanguage);
        
        Context context = setLocale(activity, currentLanguage);
        Resources resources = context.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        DisplayMetrics metrics = resources.getDisplayMetrics();
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(currentLanguage));
            activity.createConfigurationContext(configuration);
        } else {
            configuration.locale = new Locale(currentLanguage);
        }
        
        resources.updateConfiguration(configuration, metrics);
        activity.recreate();
    }
} 