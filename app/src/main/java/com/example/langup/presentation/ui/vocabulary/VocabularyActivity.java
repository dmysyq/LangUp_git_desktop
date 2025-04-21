package com.example.langup.presentation.ui.vocabulary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langup.R;
import com.example.langup.presentation.adapter.VocabularyAdapter;
import com.example.langup.data.repository.ContentManager;
import com.example.langup.domain.model.Series;
import com.example.langup.domain.model.Series.VocabularyItem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VocabularyActivity extends AppCompatActivity {
    private static final String EXTRA_SERIES = "extra_series";
    private static final Pattern VOCABULARY_PATTERN = Pattern.compile("([^\\[]+)\\s*\\[([^\\]]+)\\]\\s*-\\s*(.+)");
    
    private RecyclerView vocabularyRecyclerView;
    private VocabularyAdapter adapter;
    private Button toggleTranslationButton;
    private boolean showTranslations = false;
    private ContentManager contentManager;
    private TextView emptyView;
    private String seriesId;

    public static Intent newIntent(Context context, Series series) {
        Intent intent = new Intent(context, VocabularyActivity.class);
        intent.putExtra(EXTRA_SERIES, series);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);

        seriesId = getIntent().getStringExtra("series_id");
        if (seriesId == null) {
            finish();
            return;
        }

        contentManager = new ContentManager(this);
        vocabularyRecyclerView = findViewById(R.id.vocabulary_recycler_view);
        emptyView = findViewById(R.id.empty_view);

        setupToolbar();
        setupRecyclerView();
        loadVocabulary();
        setupTranslationToggle();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        TextView titleTextView = findViewById(R.id.titleTextView);
        Series series = contentManager.getSeriesById(seriesId);
        if (series != null) {
            titleTextView.setText(series.getTitle());
        }
    }

    private void setupRecyclerView() {
        adapter = new VocabularyAdapter();
        vocabularyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        vocabularyRecyclerView.setAdapter(adapter);
    }

    private void loadVocabulary() {
        Series series = contentManager.getSeriesById(seriesId);
        if (series != null && series.getVocabulary() != null) {
            List<VocabularyItem> vocabularyItems = series.getVocabulary();
            if (!vocabularyItems.isEmpty()) {
                adapter.setVocabularyItems(vocabularyItems);
                vocabularyRecyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            } else {
                showEmptyView();
            }
        } else {
            showEmptyView();
        }
    }

    private void showEmptyView() {
        vocabularyRecyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    private void setupTranslationToggle() {
        toggleTranslationButton = findViewById(R.id.toggleTranslationButton);
        updateToggleButton();

        toggleTranslationButton.setOnClickListener(v -> {
            showTranslations = !showTranslations;
            updateToggleButton();
            adapter.animateTranslations(showTranslations);
        });
    }

    private void updateToggleButton() {
        toggleTranslationButton.setSelected(showTranslations);
        toggleTranslationButton.setText(showTranslations ? R.string.hide_translation : R.string.show_translation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 