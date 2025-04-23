package com.example.langup.domain.model;

import java.util.List;
import java.util.stream.Collectors;

public class SeriesFilter {
    private String searchQuery;
    private final int difficultyLevel;
    private String accent;

    public SeriesFilter(String searchQuery, int difficultyLevel, String accent) {
        this.searchQuery = searchQuery.toLowerCase();
        this.difficultyLevel = difficultyLevel;
        this.accent = accent;
    }

    public List<Series> filter(List<Series> series) {
        return series.stream()
                .filter(this::matchesSearchQuery)
                .filter(this::matchesDifficulty)
                .filter(this::matchesAccent)
                .collect(Collectors.toList());
    }

    private boolean matchesSearchQuery(Series series) {
        if (searchQuery.isEmpty()) return true;
        return series.getTitle().toLowerCase().contains(searchQuery) ||
               series.getDescription().toLowerCase().contains(searchQuery);
    }

    private boolean matchesDifficulty(Series series) {
        if (difficultyLevel <= 0) return true;
        return series.getDifficulty() == difficultyLevel;
    }

    private boolean matchesAccent(Series series) {
        if (accent == null || accent.isEmpty()) return true;
        return series.getAccent().equals(accent);
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }
} 