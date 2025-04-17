package com.example.langup.utils;

import java.util.HashMap;
import java.util.Map;

public class PreferencesTranslations {
    private static final Map<String, String> TRANSLATIONS = new HashMap<>();

    static {
        // Genres
        TRANSLATIONS.put("Fantasy", "Фантастика");
        TRANSLATIONS.put("Adventure", "Приключения");
        TRANSLATIONS.put("Comedy", "Комедия");
        TRANSLATIONS.put("Drama", "Драма");
        TRANSLATIONS.put("Action", "Боевик");
        TRANSLATIONS.put("Thriller", "Триллер");
        TRANSLATIONS.put("Horror", "Ужасы");
        TRANSLATIONS.put("Animation", "Анимация");
        TRANSLATIONS.put("Documentary", "Документальный");
        TRANSLATIONS.put("Family", "Семейный");

        // Countries
        TRANSLATIONS.put("USA", "США");
        TRANSLATIONS.put("Japan", "Япония");
        TRANSLATIONS.put("UK", "Великобритания");
        TRANSLATIONS.put("France", "Франция");
        TRANSLATIONS.put("Germany", "Германия");
        TRANSLATIONS.put("Italy", "Италия");
        TRANSLATIONS.put("South Korea", "Южная Корея");
        TRANSLATIONS.put("India", "Индия");
        TRANSLATIONS.put("Russia", "Россия");
        TRANSLATIONS.put("Australia", "Австралия");

        // Franchises
        TRANSLATIONS.put("Star Wars", "Звёздные войны");
        TRANSLATIONS.put("Marvel", "Марвел");
        TRANSLATIONS.put("DC Comics", "DC Comics");
        TRANSLATIONS.put("Harry Potter", "Гарри Поттер");
        TRANSLATIONS.put("Lord of the Rings", "Властелин колец");
        TRANSLATIONS.put("Fast &amp; Furious", "Форсаж");
        TRANSLATIONS.put("Mission: Impossible", "Миссия невыполнима");
        TRANSLATIONS.put("James Bond", "Джеймс Бонд");
        TRANSLATIONS.put("Transformers", "Трансформеры");
        TRANSLATIONS.put("Pirates of the Caribbean", "Пираты Карибского моря");
    }

    public static String getRussianTranslation(String englishText) {
        return TRANSLATIONS.getOrDefault(englishText, englishText);
    }

    public static String getEnglishTranslation(String russianText) {
        for (Map.Entry<String, String> entry : TRANSLATIONS.entrySet()) {
            if (entry.getValue().equals(russianText)) {
                return entry.getKey();
            }
        }
        return russianText;
    }
} 