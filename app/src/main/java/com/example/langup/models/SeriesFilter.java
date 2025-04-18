package com.example.langup.models;

import java.util.List;
import java.util.stream.Collectors;

public class SeriesFilter {
    private String searchQuery;
    private int difficultyLevel;
    private String accent;

    public SeriesFilter(String searchQuery, int difficultyLevel, String accent) {
        this.searchQuery = searchQuery.toLowerCase();
        this.difficultyLevel = difficultyLevel;
        this.accent = accent;
    }

    public List<Series> filter(List<Series> series) {
        return series.stream()
                .filter(s -> matchesSearchQuery(s))
                .filter(s -> matchesDifficulty(s))
                .filter(s -> matchesAccent(s))
                .collect(Collectors.toList());
    }

    private boolean matchesSearchQuery(Series series) {
        if (searchQuery.isEmpty()) return true;
        return series.getTitle().toLowerCase().contains(searchQuery) ||
               series.getDescription().toLowerCase().contains(searchQuery);
    }

    private boolean matchesDifficulty(Series series) {
        if (difficultyLevel <= 0) return true;
        return series.getLevel().equals(getLevelString(difficultyLevel));
    }

    private boolean matchesAccent(Series series) {
        if (accent == null || accent.isEmpty()) return true;
        return series.getAccent().equals(accent);
    }

    private String getLevelString(int level) {
        switch (level) {
            case 1: return "beginner";
            case 2: return "intermediate";
            case 3: return "advanced";
            default: return "beginner";
        }
    }
} 