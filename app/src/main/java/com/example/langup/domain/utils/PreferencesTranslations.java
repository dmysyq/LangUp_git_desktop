package com.example.langup.domain.utils;

import java.util.HashMap;
import java.util.Map;

public class PreferencesTranslations {
    private static final Map<String, String> translations = new HashMap<>();
    
    static {
        // Genres
        translations.put("Action", "Экшен");
        translations.put("Adventure", "Приключения");
        translations.put("Comedy", "Комедия");
        translations.put("Drama", "Драма");
        translations.put("Fantasy", "Фэнтези");
        translations.put("Horror", "Ужасы");
        translations.put("Mystery", "Мистика");
        translations.put("Romance", "Романтика");
        translations.put("Sci-Fi", "Научная фантастика");
        translations.put("Thriller", "Триллер");
        
        // Countries
        translations.put("USA", "США");
        translations.put("UK", "Великобритания");
        translations.put("Japan", "Япония");
        translations.put("South Korea", "Южная Корея");
        translations.put("China", "Китай");
        translations.put("France", "Франция");
        translations.put("Germany", "Германия");
        translations.put("Italy", "Италия");
        translations.put("Spain", "Испания");
        translations.put("Russia", "Россия");
        
        // Franchises
        translations.put("Marvel", "Марвел");
        translations.put("DC", "DC");
        translations.put("Star Wars", "Звёздные войны");
        translations.put("Star Trek", "Звёздный путь");
        translations.put("Harry Potter", "Гарри Поттер");
        translations.put("Lord of the Rings", "Властелин колец");
        translations.put("Game of Thrones", "Игра престолов");
        translations.put("The Witcher", "Ведьмак");
        translations.put("The Matrix", "Матрица");
        translations.put("Fast & Furious", "Форсаж");
    }
    
    public static String getRussianTranslation(String english) {
        return translations.getOrDefault(english, english);
    }
    
    public static String getEnglishTranslation(String russian) {
        for (Map.Entry<String, String> entry : translations.entrySet()) {
            if (entry.getValue().equals(russian)) {
                return entry.getKey();
            }
        }
        return russian;
    }
    
    public static boolean isEnglish(String text) {
        return translations.containsKey(text);
    }
    
    public static boolean isRussian(String text) {
        return translations.containsValue(text);
    }
} 