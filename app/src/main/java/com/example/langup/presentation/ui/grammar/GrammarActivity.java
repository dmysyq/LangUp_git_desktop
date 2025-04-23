package com.example.langup.presentation.ui.grammar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.langup.R;
import com.example.langup.presentation.ui.grammar.GrammarExercise;
import com.example.langup.presentation.adapter.GrammarAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class GrammarActivity extends AppCompatActivity {
    private static final String TAG = "GrammarActivity";
    private TextView titleTextView;
    private RecyclerView grammarRecyclerView;
    private GrammarAdapter adapter;
    private List<GrammarExercise> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar);
        Log.d(TAG, "onCreate: Starting GrammarActivity");

        String title = getIntent().getStringExtra("title");
        String grammarJson = getIntent().getStringExtra("grammar");
        Log.d(TAG, "Received title: " + title);
        Log.d(TAG, "Received grammar JSON: " + grammarJson);

        initializeViews();
        setupToolbar(title);
        loadGrammarExercises(grammarJson);
    }

    private void initializeViews() {
        titleTextView = findViewById(R.id.titleTextView);
        grammarRecyclerView = findViewById(R.id.grammarRecyclerView);
        grammarRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleTextView.setText(title);
    }

    private void loadGrammarExercises(String grammarJson) {
        try {
            if (grammarJson != null) {
                Gson gson = new Gson();
                List<Grammar> grammarList = gson.fromJson(grammarJson, new TypeToken<List<Grammar>>(){}.getType());
                if (grammarList != null && !grammarList.isEmpty()) {
                    Grammar grammar = grammarList.get(0); // Берем первый элемент, так как у нас массив из одного объекта
                    exercises = grammar.getExercises();
                    Log.d(TAG, "Loaded " + exercises.size() + " grammar exercises");
                    
                    adapter = new GrammarAdapter(exercises);
                    grammarRecyclerView.setAdapter(adapter);
                } else {
                    Log.e(TAG, "Grammar list is empty");
                    showError("Error loading grammar exercises");
                }
            } else {
                Log.e(TAG, "Grammar JSON is null");
                showError("Error loading grammar exercises");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing grammar JSON: " + e.getMessage());
            showError("Error parsing grammar exercises");
        }
    }

    private static class Grammar {
        private List<GrammarExercise> exercises;
        private String explanation;
        private String topic;

        public List<GrammarExercise> getExercises() {
            return exercises;
        }
    }

    private void showError(String message) {
        Snackbar.make(grammarRecyclerView, message, Snackbar.LENGTH_LONG).show();
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