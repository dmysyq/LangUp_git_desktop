package com.example.langup.presentation.ui.vocabulary;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langup.R;
import com.example.langup.domain.model.VocabularyItem;
import com.example.langup.presentation.adapter.VocabularyAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.langup.presentation.base.BaseActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VocabularyActivity extends BaseActivity {
    private static final String TAG = "VocabularyActivity";
    
    private TextView titleTextView;
    private RecyclerView vocabularyRecyclerView;
    private VocabularyAdapter adapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_vocabulary;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get data from intent
        String title = getIntent().getStringExtra("title");
        String vocabularyJson = getIntent().getStringExtra("vocabulary");

        Log.d(TAG, "onCreate: Received vocabulary data length: " + 
            (vocabularyJson != null ? vocabularyJson.length() : 0));

        initializeViews();
        setupToolbar(title);
        setupRecyclerView();
        loadVocabulary(vocabularyJson);
    }

    private void initializeViews() {
        titleTextView = findViewById(R.id.titleTextView);
        vocabularyRecyclerView = findViewById(R.id.vocabularyRecyclerView);
        ImageButton translateButton = findViewById(R.id.translateButton);

        translateButton.setOnClickListener(v -> toggleTranslation());
    }

    private void setupToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            titleTextView.setText(title);
        }
    }

    private void setupRecyclerView() {
        vocabularyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VocabularyAdapter(new ArrayList<>());
        vocabularyRecyclerView.setAdapter(adapter);
    }

    private void loadVocabulary(String vocabularyJson) {
        if (vocabularyJson != null && !vocabularyJson.isEmpty()) {
            try {
                Type listType = new TypeToken<List<VocabularyItem>>(){}.getType();
                List<VocabularyItem> vocabulary = new Gson().fromJson(vocabularyJson, listType);
                adapter = new VocabularyAdapter(vocabulary);
                vocabularyRecyclerView.setAdapter(adapter);
            } catch (Exception e) {
                Log.e(TAG, "Error parsing vocabulary JSON: " + e.getMessage());
                showError(R.string.error_loading_vocabulary);
            }
        } else {
            showError(R.string.error_loading_vocabulary);
        }
    }

    private void toggleTranslation() {
        if (adapter != null) {
            adapter.toggleTranslation();
        }
    }

    private void showError(int messageResId) {
        Snackbar.make(vocabularyRecyclerView, messageResId, Snackbar.LENGTH_LONG).show();
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