package com.example.langup.presentation.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langup.domain.model.Series;
import com.example.langup.presentation.adapter.SeriesAdapter;
import com.example.langup.presentation.ui.profile.UserProfileActivity;
import com.example.langup.presentation.ui.profile.SettingsActivity;
import com.example.langup.presentation.ui.auth.WelcomeActivity;
import com.example.langup.R;
import com.example.langup.presentation.ui.dialogs.SearchFilterDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.example.langup.data.local.JsonLoader;
import com.example.langup.presentation.ui.level.LevelSelectionActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements SeriesAdapter.OnSeriesClickListener, SearchFilterDialog.OnSearchFilterListener {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private SeriesAdapter adapter;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private ImageButton navHome;
    private ImageButton navProfile;
    private ImageButton navSettings;
    private List<Series> allSeries = new ArrayList<>();
    private int selectedDifficulty = 0;
    private String selectedAccent = "";
    private String selectedSource = "";

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
        loadSeries();
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
            // Already on home
        });

        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, UserProfileActivity.class));
        });

        navSettings.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });
    }

    private void loadSeries() {
        progressBar.setVisibility(View.VISIBLE);
        
        JsonLoader jsonLoader = new JsonLoader(this);
        jsonLoader.loadSeries(new JsonLoader.SeriesCallback() {
            @Override
            public void onSuccess(List<Series> series) {
                allSeries = series;
                Log.d("MainActivity", "Loaded " + series.size() + " series");
                applyFilters();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(String error) {
                Log.e("MainActivity", "Error loading series: " + error);
                Toast.makeText(MainActivity.this, R.string.error_loading_series, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void applyFilters() {
        Log.d("MainActivity", "Applying filters: difficulty=" + selectedDifficulty + 
              ", accent='" + selectedAccent + "', source='" + selectedSource + "'");
        
        List<Series> filteredSeries = allSeries.stream()
            .filter(series -> selectedDifficulty == 0 || series.getMetadata().getDifficulty() == selectedDifficulty)
            .filter(series -> selectedAccent.isEmpty() || series.getMetadata().getAccent().equals(selectedAccent))
            .filter(series -> selectedSource.isEmpty() || series.getMetadata().getSource().equals(selectedSource))
            .collect(Collectors.toList());
        
        Log.d("MainActivity", "Filtered series count: " + filteredSeries.size());
        adapter.updateSeries(filteredSeries);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    @Override
    public void onSeriesClick(Series series) {
        Intent intent = new Intent(this, LevelSelectionActivity.class);
        intent.putExtra("series_id", series.getId());
        intent.putExtra("title", series.getTitle());
        intent.putExtra("description", series.getDescription());
        intent.putExtra("difficulty", series.getDifficulty());
        intent.putExtra("accent", series.getAccent());
        intent.putExtra("video_url", series.getVideoUrl());
        intent.putExtra("transcript", series.getContent().getTranscript().getFull());
        intent.putExtra("vocabulary", new Gson().toJson(series.getContent().getVocabulary()));
        intent.putExtra("questions", new Gson().toJson(series.getContent().getQuestions()));
        Log.d(TAG, "Starting LevelSelectionActivity with series ID: " + series.getId());
        startActivity(intent);
    }

    @Override
    public void onSearchFilter(int difficultyLevel, String accent, String source) {
        selectedDifficulty = difficultyLevel;
        selectedAccent = accent;
        selectedSource = source;
        applyFilters();
    }

    @Override
    public void onResetFilters() {
        selectedDifficulty = 0;
        selectedAccent = "";
        selectedSource = "";
        applyFilters();
    }
}
