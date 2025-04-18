package com.example.langup.utils;

import com.example.langup.models.Series;
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
            .filter(this::matchesSearch)
            .filter(this::matchesDifficulty)
            .filter(this::matchesAccent)
            .collect(Collectors.toList());
    }

    private boolean matchesSearch(Series series) {
        if (searchQuery.isEmpty()) {
            return true;
        }
        return series.getTitle().toLowerCase().contains(searchQuery) ||
               series.getDescription().toLowerCase().contains(searchQuery);
    }

    private boolean matchesDifficulty(Series series) {
        if (difficultyLevel == 0) {
            return true;
        }
        return series.getEpisodes().stream()
            .anyMatch(episode -> episode.getDifficulty() == difficultyLevel);
    }

    private boolean matchesAccent(Series series) {
        if (accent.isEmpty()) {
            return true;
        }
        return series.getAccent().equals(accent);
    }
} 