package com.example.langup.presentation.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.LinearLayout;

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
import com.example.langup.presentation.base.BaseActivity;
import com.example.langup.presentation.adapter.LanguageSelectorAdapter;
import com.example.langup.presentation.model.LanguageItem;
import com.example.langup.data.repository.SubscriptionManager;
import com.example.langup.domain.model.Subscription;
import com.example.langup.presentation.ui.premium.SubscribeActivity;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

import java.util.Objects;
import java.util.stream.Collectors;

public class MainActivity extends BaseActivity implements SeriesAdapter.OnSeriesClickListener, SearchFilterDialog.OnSearchFilterListener {
    private static final String TAG = "MainActivity";
    private SeriesAdapter adapter;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private List<Series> allSeries = new ArrayList<>();
    private int selectedDifficulty = 0;
    private String selectedAccent = "";
    private String selectedSource = "";
    private String currentLanguage = "en"; // Default language is English
    private MenuItem filterMenuItem; // Store reference to filter menu item
    private SubscriptionManager subscriptionManager;
    private static final int REQUEST_SUBSCRIBE = 102;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

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
        
        initializeViews();
        setupToolbar();
        setupNavigation();
        setupLanguageSelector();
        subscriptionManager = new SubscriptionManager(this);
        loadSeries();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new SeriesAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        
        // Create layout for toolbar content
        LinearLayout toolbarLayout = new LinearLayout(this);
        toolbarLayout.setLayoutParams(new Toolbar.LayoutParams(
            Toolbar.LayoutParams.MATCH_PARENT,
            Toolbar.LayoutParams.WRAP_CONTENT
        ));
        toolbarLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Add language spinner to the left
        Spinner languageSpinner = findViewById(R.id.languageSpinner);
        if (languageSpinner != null && languageSpinner.getParent() != null) {
            ((ViewGroup) languageSpinner.getParent()).removeView(languageSpinner);
            LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            spinnerParams.setMarginStart(16); // Add some margin from the left edge
            languageSpinner.setLayoutParams(spinnerParams);
            toolbarLayout.addView(languageSpinner);
        }

        // Add title in the center
        TextView titleView = new TextView(this);
        titleView.setText(R.string.app_name);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.gravity = android.view.Gravity.CENTER;
        titleParams.leftMargin = 248; // Adjust this value to fine-tune the position
        titleParams.rightMargin = 248; // Adjust this value to fine-tune the position
        titleView.setLayoutParams(titleParams);
        titleView.setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Headline6);
        titleView.setTextColor(getResources().getColor(R.color.text_tertiary_light, null));
        titleView.setTextSize(20);
        toolbarLayout.addView(titleView);

        // Add the layout to toolbar
        toolbar.addView(toolbarLayout);
    }

    private void setupNavigation() {
        ImageButton homeButton = findViewById(R.id.nav_home);
        ImageButton profileButton = findViewById(R.id.nav_profile);
        ImageButton settingsButton = findViewById(R.id.nav_settings);

        homeButton.setOnClickListener(v -> {
            // Already on home
        });

        profileButton.setOnClickListener(v -> {
            startActivity(new Intent(this, UserProfileActivity.class));
        });

        settingsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });
    }

    private void setupLanguageSelector() {
        List<LanguageItem> languageItems = new ArrayList<>();
        languageItems.add(new LanguageItem("en", R.drawable.ic_flag_us));
        languageItems.add(new LanguageItem("ru", R.drawable.ic_flag_ru));
        languageItems.add(new LanguageItem("fr", R.drawable.ic_flag_fr));
        languageItems.add(new LanguageItem("es", R.drawable.ic_flag_es));
        languageItems.add(new LanguageItem("kk", R.drawable.ic_flag_kk));

        Spinner languageSpinner = findViewById(R.id.languageSpinner);
        LanguageSelectorAdapter adapter = new LanguageSelectorAdapter(this, languageItems);
        languageSpinner.setAdapter(adapter);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LanguageItem selectedItem = (LanguageItem) parent.getItemAtPosition(position);
                currentLanguage = selectedItem.getCode();
                filterSeriesByLanguage(currentLanguage);
                updateFilterButtonState();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void updateFilterButtonState() {
        if (filterMenuItem != null) {
            boolean isEnglish = "en".equals(currentLanguage);
            filterMenuItem.setEnabled(isEnglish);
            filterMenuItem.setVisible(isEnglish);
            
            // Reset filters if switching from English to another language
            if (!isEnglish) {
                onResetFilters();
            }
        }
    }

    private void filterSeriesByLanguage(String languageCode) {
        List<Series> filteredSeries = allSeries.stream()
            .filter(series -> {
                String lang = series.getMetadata().getLang();
                return lang != null && lang.equals(languageCode);
            })
            .collect(Collectors.toList());
        adapter.updateSeries(filteredSeries);
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
        // Only apply filters if current language is English
        if (!"en".equals(currentLanguage)) {
            return;
        }

        Log.d("MainActivity", "Applying filters: difficulty=" + selectedDifficulty + 
              ", accent='" + selectedAccent + "', source='" + selectedSource + "'");
        
        List<Series> filteredSeries = allSeries.stream()
            .filter(series -> {
                String lang = series.getMetadata().getLang();
                if (lang == null || !lang.equals(currentLanguage)) {
                    return false;
                }
                return (selectedDifficulty == 0 || series.getMetadata().getDifficulty() == selectedDifficulty) &&
                       (selectedAccent.isEmpty() || series.getMetadata().getAccent().equals(selectedAccent)) &&
                       (selectedSource.isEmpty() || series.getMetadata().getSource().equals(selectedSource));
            })
            .collect(Collectors.toList());
        
        Log.d("MainActivity", "Filtered series count: " + filteredSeries.size());
        adapter.updateSeries(filteredSeries);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        filterMenuItem = menu.findItem(R.id.action_filter);
        updateFilterButtonState();
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
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    @Override
    public void onSeriesClick(Series series) {
        if (series.getMetadata().isPremium()) {
            subscriptionManager.checkSubscription(new SubscriptionManager.SubscriptionCallback() {
                @Override
                public void onSuccess(Subscription subscription) {
                    // User is subscribed, open the series
                    startSeriesActivity(series);
                }

                @Override
                public void onError(String error) {
                    // User is not subscribed or error occurred
                    Toast.makeText(MainActivity.this, R.string.premium_content_requires_subscription, Toast.LENGTH_SHORT).show();
                    openSubscribeActivity();
                }
            });
        } else {
            // Not a premium series, open directly
            startSeriesActivity(series);
        }
    }

    private void startSeriesActivity(Series series) {
        // Replace placeholder with actual logic to launch LevelSelectionActivity
        Intent intent = new Intent(this, LevelSelectionActivity.class);
        intent.putExtra("series_id", series.getId());
        intent.putExtra("title", series.getMetadata().getTitle());
        intent.putExtra("description", series.getMetadata().getDescription());
        intent.putExtra("difficulty", series.getMetadata().getDifficulty());
        intent.putExtra("accent", series.getMetadata().getAccent());
        intent.putExtra("video_url", series.getMetadata().getVideoUrl());

        // Handle null transcript
        String transcript = "";
        if (series.getContent() != null && series.getContent().getTranscript() != null) {
            transcript = series.getContent().getTranscript().getFull();
        }
        intent.putExtra("transcript", transcript);

        // Handle null content
        if (series.getContent() != null) {
            intent.putExtra("vocabulary", new Gson().toJson(series.getContent().getVocabulary()));
            intent.putExtra("questions", new Gson().toJson(series.getContent().getQuestions()));
            intent.putExtra("grammar", new Gson().toJson(series.getContent().getGrammar()));
        } else {
            intent.putExtra("vocabulary", "[]");
            intent.putExtra("questions", "[]");
            intent.putExtra("grammar", "[]");
        }

        Log.d(TAG, "Starting LevelSelectionActivity with series ID: " + series.getId());
        startActivity(intent);
    }

    private void openSubscribeActivity() {
        Intent intent = new Intent(this, com.example.langup.presentation.ui.premium.SubscribeActivity.class);
        startActivityForResult(intent, REQUEST_SUBSCRIBE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SUBSCRIBE && resultCode == RESULT_OK) {
            // Subscription successful, refresh UI if needed (optional here, as check happens on resume)
            // For now, just log or show a message
            Log.d(TAG, "Subscription successful, returning from SubscribeActivity");
        }
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
