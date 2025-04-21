package com.example.langup.domain.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

public class LocaleManager {
    private static final String LANGUAGE_KEY = "language_key";
    private static final String LANGUAGE_SYSTEM = "system";
    private static final String LANGUAGE_EN = "en";
    private static final String LANGUAGE_RU = "ru";

    private static volatile LocaleManager instance;
    private SharedPreferences preferences;
    private Context context;

    private LocaleManager() {
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
                    // В случае ошибки оставляем preferences = null
                }
            }
        }
    }

    public void setLocale(String language) {
        if (preferences != null) {
            preferences.edit().putString(LANGUAGE_KEY, language).apply();
            updateConfiguration(language);
        }
    }

    private Context setLocale(Context context, String language) {
        if (context == null) return context;

        try {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);

            Configuration configuration = new Configuration(context.getResources().getConfiguration());
            configuration.setLocale(locale);

            return context.createConfigurationContext(configuration);
        } catch (Exception e) {
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
            return language.equals(LANGUAGE_RU) ? LANGUAGE_RU : LANGUAGE_EN;
        } catch (Exception e) {
            return LANGUAGE_EN;
        }
    }

    private void updateConfiguration(String language) {
        if (context == null) return;

        try {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);

            Configuration configuration = new Configuration(context.getResources().getConfiguration());
            configuration.setLocale(locale);

            context.createConfigurationContext(configuration);
            context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
        } catch (Exception e) {
            // Игнорируем ошибки при обновлении конфигурации
        }
    }

    public void applyCurrentLocale() {
        if (context == null) return;
        String currentLanguage = getCurrentLanguage();
        updateConfiguration(currentLanguage);
    }

    public String translateToEnglish(String russianText) {
        return PreferencesTranslations.getEnglishTranslation(russianText);
    }

    public String translateToRussian(String englishText) {
        return PreferencesTranslations.getRussianTranslation(englishText);
    }

    public void setSystemLocale() {
        if (preferences != null) {
            preferences.edit().putString(LANGUAGE_KEY, LANGUAGE_SYSTEM).apply();
            String systemLanguage = getSystemLanguage();
            updateConfiguration(systemLanguage);
        }
    }

    public Locale getCurrentLocale() {
        return new Locale(getCurrentLanguage());
    }
} 