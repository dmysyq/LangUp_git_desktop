package com.example.langup.presentation.ui.main;

import com.example.langup.domain.model.SeriesMetadata;
import java.util.List;
import java.util.stream.Collectors;

public class FilterManager {
    private String searchQuery = "";
    private String selectedCountry = "";
    private String selectedAccent = "";
    private int selectedDifficulty = 0;

    public void setSearchQuery(String query) {
        this.searchQuery = query.toLowerCase();
    }

    public void setSelectedCountry(String country) {
        this.selectedCountry = country;
    }

    public void setSelectedAccent(String accent) {
        this.selectedAccent = accent;
    }

    public void setSelectedDifficulty(int difficulty) {
        this.selectedDifficulty = difficulty;
    }

    public List<SeriesMetadata> filter(List<SeriesMetadata> seriesList) {
        return seriesList.stream()
                .filter(this::matchesSearchQuery)
                .filter(this::matchesCountry)
                .filter(this::matchesAccent)
                .filter(this::matchesDifficulty)
                .collect(Collectors.toList());
    }

    private boolean matchesSearchQuery(SeriesMetadata series) {
        if (searchQuery.isEmpty()) return true;
        return series.getTitle().toLowerCase().contains(searchQuery) ||
               series.getDescription().toLowerCase().contains(searchQuery);
    }

    private boolean matchesCountry(SeriesMetadata series) {
        if (selectedCountry.isEmpty()) return true;
        return series.getCountry().equals(selectedCountry);
    }

    private boolean matchesAccent(SeriesMetadata series) {
        if (selectedAccent.isEmpty()) return true;
        return series.getAccent().equals(selectedAccent);
    }

    private boolean matchesDifficulty(SeriesMetadata series) {
        if (selectedDifficulty == 0) return true;
        return series.getDifficulty() == selectedDifficulty;
    }

    public void resetFilters() {
        searchQuery = "";
        selectedCountry = "";
        selectedAccent = "";
        selectedDifficulty = 0;
    }
} 