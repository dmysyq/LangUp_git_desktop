package com.example.langup.presentation.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.langup.domain.model.Series;
import com.example.langup.domain.model.SeriesMetadata;
import com.example.langup.data.repository.ContentManager;
import com.example.langup.data.local.PreferencesManager;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Comparator;

public class SeriesViewModel extends ViewModel {
    private final MutableLiveData<CardState> cardState = new MutableLiveData<>();
    private final FilterManager filterManager = new FilterManager();
    private List<SeriesMetadata> allSeriesMetadata = new ArrayList<>();
    private ContentManager contentManager;
    private PreferencesManager preferencesManager;

    public SeriesViewModel(ContentManager contentManager, PreferencesManager preferencesManager) {
        this.contentManager = contentManager;
        this.preferencesManager = preferencesManager;
        loadSeries();
    }

    public LiveData<CardState> getCardState() {
        return cardState;
    }

    public void loadSeries() {
        cardState.setValue(CardState.loading());
        try {
            List<Series> seriesList = contentManager.getAllSeries();
            allSeriesMetadata = seriesList.stream()
                    .map(Series::getMetadata)
                    .collect(Collectors.toList());
            applyFilters();
        } catch (Exception e) {
            cardState.setValue(CardState.error(e.getMessage()));
        }
    }

    public void setSearchQuery(String query) {
        filterManager.setSearchQuery(query);
        applyFilters();
    }

    public void setSelectedCountry(String country) {
        filterManager.setSelectedCountry(country);
        applyFilters();
    }

    public void setSelectedAccent(String accent) {
        filterManager.setSelectedAccent(accent);
        applyFilters();
    }

    public void setSelectedDifficulty(int difficulty) {
        filterManager.setSelectedDifficulty(difficulty);
        applyFilters();
    }

    public void resetFilters() {
        filterManager.resetFilters();
        applyFilters();
    }

    private void applyFilters() {
        List<SeriesMetadata> filteredList = filterManager.filter(allSeriesMetadata);
        
        // Apply user preferences ranking
        List<String> preferredGenres = preferencesManager.getGenres();
        List<String> preferredCountries = preferencesManager.getCountries();
        List<String> preferredSources = preferencesManager.getSources();
        
        filteredList.sort((a, b) -> {
            int scoreA = calculatePreferenceScore(a, preferredGenres, preferredCountries, preferredSources);
            int scoreB = calculatePreferenceScore(b, preferredGenres, preferredCountries, preferredSources);
            return Integer.compare(scoreB, scoreA); // Higher score first
        });
        
        cardState.setValue(CardState.success(filteredList));
    }
    
    private int calculatePreferenceScore(SeriesMetadata series, 
                                       List<String> preferredGenres,
                                       List<String> preferredCountries,
                                       List<String> preferredSources) {
        int score = 0;
        
        // Check genres match
        if (series.getGenres() != null) {
            for (String genre : series.getGenres()) {
                if (preferredGenres.contains(genre)) {
                    score += 2; // Higher weight for genre matches
                }
            }
        }
        
        // Check country match
        if (series.getCountry() != null && preferredCountries.contains(series.getCountry())) {
            score += 1;
        }
        
        // Check source match
        if (series.getSource() != null && preferredSources.contains(series.getSource())) {
            score += 1;
        }
        
        return score;
    }
} 