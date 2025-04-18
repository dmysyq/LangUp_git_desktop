package com.example.langup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langup.adapters.VocabularyAdapter;
import com.example.langup.models.Series;
import com.example.langup.models.VocabularyItem;

import java.util.List;

public class VocabularyActivity extends AppCompatActivity {
    private static final String EXTRA_SERIES = "extra_series";
    
    private RecyclerView vocabularyRecyclerView;
    private VocabularyAdapter adapter;
    private Button toggleTranslationButton;
    private boolean showTranslations = false;

    public static Intent newIntent(Context context, Series series) {
        Intent intent = new Intent(context, VocabularyActivity.class);
        intent.putExtra(EXTRA_SERIES, series);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);

        Series series = (Series) getIntent().getSerializableExtra(EXTRA_SERIES);
        if (series == null) {
            finish();
            return;
        }

        setupToolbar(series.getTitle());
        setupRecyclerView(series.getVocabulary());
        setupTranslationToggle();
    }

    private void setupToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(title);
    }

    private void setupRecyclerView(List<VocabularyItem> vocabulary) {
        vocabularyRecyclerView = findViewById(R.id.vocabularyRecyclerView);
        vocabularyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VocabularyAdapter(vocabulary);
        vocabularyRecyclerView.setAdapter(adapter);
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
        toggleTranslationButton.setText(showTranslations ? "Hide Translation" : "Show Translation");
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