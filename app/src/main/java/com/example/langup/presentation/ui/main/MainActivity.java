package com.example.langup.presentation.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langup.domain.model.Series;
import com.example.langup.presentation.adapter.SeriesAdapter;
import com.example.langup.presentation.ui.profile.UserProfileActivity;
import com.example.langup.presentation.ui.profile.SettingsActivity;
import com.example.langup.presentation.ui.auth.WelcomeActivity;
import com.example.langup.R;
import com.example.langup.presentation.ui.dialogs.SearchFilterDialog;
import com.example.langup.domain.utils.RecommendationEngine;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SeriesAdapter.OnSeriesClickListener, SearchFilterDialog.OnSearchFilterListener {
    private RecyclerView recyclerView;
    private SeriesAdapter adapter;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private ImageButton navHome;
    private ImageButton navProfile;
    private ImageButton navSettings;
    private RecommendationEngine recommendationEngine;
    private List<Series> allSeries = new ArrayList<>();
    private String searchQuery = "";
    private int selectedDifficulty = 0;
    private String selectedAccent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Check if user is authenticated
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // User is not authenticated, redirect to WelcomeActivity
            startActivity(new Intent(this, WelcomeActivity.class));
            finish(); // Close MainActivity
            return;
        }
        
        setContentView(R.layout.activity_main);
        initializeViews();
        setupToolbar();
        setupNavigation();
        setupRecommendationEngine();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        navHome = findViewById(R.id.nav_home);
        navProfile = findViewById(R.id.nav_profile);
        navSettings = findViewById(R.id.nav_settings);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new SeriesAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
    }

    private void setupNavigation() {
        navHome.setOnClickListener(v -> {
            // Already on home, do nothing
        });
        
        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, UserProfileActivity.class));
        });
        
        navSettings.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });
    }

    private void setupRecommendationEngine() {
        recommendationEngine = new RecommendationEngine(this);
        loadRecommendations();
    }

    private void loadRecommendations() {
        progressBar.setVisibility(View.VISIBLE);
        recommendationEngine.getRecommendations(new RecommendationEngine.RecommendationCallback() {
            @Override
            public void onSuccess(List<Series> recommendations) {
                allSeries = recommendations;
                applyFilters();
                progressBar.setVisibility(View.GONE);
            }
            
            @Override
            public void onError(String error) {
                Toast.makeText(MainActivity.this, "Error loading recommendations: " + error, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void applyFilters() {
        List<Series> filteredSeries = new ArrayList<>();
        
        for (Series series : allSeries) {
            boolean matchesSearch = searchQuery.isEmpty() || 
                                   series.getMetadata().getTitle().toLowerCase().contains(searchQuery.toLowerCase());
            
            boolean matchesDifficulty = selectedDifficulty == 0 || 
                                       series.getDifficulty() == selectedDifficulty;
            
            boolean matchesAccent = selectedAccent.isEmpty() || 
                                   (series.getMetadata().getAccent() != null && 
                                    series.getMetadata().getAccent().equals(selectedAccent));
            
            if (matchesSearch && matchesDifficulty && matchesAccent) {
                filteredSeries.add(series);
            }
        }
        
        adapter.updateSeries(filteredSeries);
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
                applyFilters();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                applyFilters();
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

    private void showSearchFilterDialog() {
        SearchFilterDialog dialog = new SearchFilterDialog(this, this);
        dialog.show();
    }

    @Override
    public void onSeriesClick(Series series) {
        // Здесь будет переход к выбору уровня серии
        // Intent intent = new Intent(this, LevelSelectionActivity.class);
        // intent.putExtra("seriesId", series.getId());
        // startActivity(intent);
        
        Toast.makeText(this, "Selected: " + series.getMetadata().getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchFilter(String searchQuery, int difficultyLevel, String accent) {
        this.searchQuery = searchQuery;
        this.selectedDifficulty = difficultyLevel;
        this.selectedAccent = accent;
        applyFilters();
    }
}
