package com.example.langup;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.langup.adapters.SeriesAdapter;
import com.example.langup.dialogs.SearchFilterDialog;
import com.example.langup.models.Series;
import com.example.langup.utils.ContentManager;
import com.example.langup.utils.PreferencesManager;
import com.example.langup.utils.SeriesFilter;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SeriesAdapter.OnSeriesClickListener, SearchFilterDialog.OnSearchFilterListener {
    private RecyclerView recyclerView;
    private SeriesAdapter adapter;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private ContentManager contentManager;
    private PreferencesManager preferencesManager;
    private List<Series> allSeries;
    private List<Series> filteredSeries;
    private SeriesFilter currentFilter;
    private String searchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupToolbar();
        loadUserPreferences();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        contentManager = new ContentManager(this);
        
        // Check if user is logged in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            preferencesManager = new PreferencesManager(this, FirebaseAuth.getInstance().getCurrentUser().getUid());
        } else {
            // Use a default user ID for testing
            preferencesManager = new PreferencesManager(this, "test_user");
        }

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new SeriesAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
    }

    private void loadUserPreferences() {
        progressBar.setVisibility(View.VISIBLE);
        preferencesManager.loadPreferences(new PreferencesManager.PreferencesCallback() {
            @Override
            public void onSuccess() {
                loadSeriesData();
            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
                loadSeriesData(); // Load series even if preferences fail
            }
            
            @Override
            public void onPreferencesLoaded(Map<String, List<String>> preferences) {
                // Process loaded preferences
                if (preferences != null) {
                    List<String> genres = preferences.get("genres");
                    List<String> countries = preferences.get("countries");
                    List<String> franchises = preferences.get("franchises");
                    
                    if (genres != null) preferencesManager.setGenres(genres);
                    if (countries != null) preferencesManager.setCountries(countries);
                    if (franchises != null) preferencesManager.setFranchises(franchises);
                }
                loadSeriesData();
            }
        });
    }

    private void loadSeriesData() {
        allSeries = contentManager.getAllSeries();
        filteredSeries = new ArrayList<>(allSeries);
        
        // Apply recommendations based on user preferences
        applyRecommendations();
        
        adapter.updateSeries(filteredSeries);
        progressBar.setVisibility(View.GONE);
    }

    private void applyRecommendations() {
        // Get user preferences
        List<String> preferredGenres = preferencesManager.getGenres();
        List<String> preferredCountries = preferencesManager.getCountries();
        List<String> preferredFranchises = preferencesManager.getFranchises();

        // If user has preferences, sort series based on them
        if (!preferredGenres.isEmpty() || !preferredCountries.isEmpty() || !preferredFranchises.isEmpty()) {
            // Sort series based on preferences
            // This is a simple implementation - you might want to use a more sophisticated algorithm
            filteredSeries.sort((s1, s2) -> {
                int score1 = calculatePreferenceScore(s1, preferredGenres, preferredCountries, preferredFranchises);
                int score2 = calculatePreferenceScore(s2, preferredGenres, preferredCountries, preferredFranchises);
                return score2 - score1; // Higher score first
            });
        }
    }

    private int calculatePreferenceScore(Series series, List<String> preferredGenres, 
                                        List<String> preferredCountries, List<String> preferredFranchises) {
        int score = 0;
        
        // Check genres
        if (series.getMetadata() != null && series.getMetadata().getGenres() != null) {
            for (String genre : series.getMetadata().getGenres()) {
                if (preferredGenres.contains(genre)) {
                    score += 2;
                }
            }
        }
        
        // Check country
        if (series.getMetadata() != null && series.getMetadata().getCountry() != null) {
            if (preferredCountries.contains(series.getMetadata().getCountry())) {
                score += 3;
            }
        }
        
        // Check franchises
        if (series.getMetadata() != null && series.getMetadata().getFranchises() != null) {
            for (String franchise : series.getMetadata().getFranchises()) {
                if (preferredFranchises.contains(franchise)) {
                    score += 1;
                }
            }
        }
        
        return score;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                filterSeries();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                filterSeries();
                return true;
            }
        });
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            showSearchFilterDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filterSeries() {
        if (allSeries == null) return;
        
        if (searchQuery.isEmpty() && currentFilter == null) {
            // No filters applied, show all series
            filteredSeries = new ArrayList<>(allSeries);
        } else {
            // Apply search filter
            SeriesFilter searchFilter = new SeriesFilter(searchQuery, 0, "");
            filteredSeries = searchFilter.filter(allSeries);
            
            // Apply additional filters if they exist
            if (currentFilter != null) {
                filteredSeries = currentFilter.filter(filteredSeries);
            }
        }
        
        adapter.updateSeries(filteredSeries);
    }

    private void showSearchFilterDialog() {
        SearchFilterDialog dialog = new SearchFilterDialog(this, this);
        dialog.show();
    }

    @Override
    public void onSeriesClick(Series series) {
        Intent intent = new Intent(this, LevelSelectionActivity.class);
        intent.putExtra("seriesId", series.getId());
        startActivity(intent);
    }

    @Override
    public void onSearchFilter(String searchQuery, int difficultyLevel, String accent) {
        currentFilter = new SeriesFilter(searchQuery, difficultyLevel, accent);
        filterSeries();
    }
}
