package com.example.langup.presentation.ui.grammar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.langup.R;
import com.example.langup.presentation.adapter.GrammarAdapter;
import com.example.langup.presentation.ui.base.BaseActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class GrammarActivity extends BaseActivity {
    private static final String TAG = "GrammarActivity";
    private TextView titleTextView;
    private RecyclerView grammarRecyclerView;
    private Button resultButton;
    private List<GrammarExercise> exercises;
    private GrammarAdapter adapter;
    private TextView answeredCounter;
    private TextView resultTextView;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_grammar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String title = getIntent().getStringExtra("title");
        String grammarJson = getIntent().getStringExtra("grammar");
        Log.d(TAG, "Received title: " + title);
        Log.d(TAG, "Received grammar JSON: " + grammarJson);

        initializeViews();
        setupToolbar(title);
        loadGrammarExercises(grammarJson);
        resultButton = findViewById(R.id.resultButton);
        answeredCounter = findViewById(R.id.answeredCounter);
        resultTextView = new TextView(this);
        resultTextView.setTextAppearance(this, androidx.appcompat.R.style.TextAppearance_AppCompat_Large);
        resultTextView.setVisibility(View.GONE);
        ((ViewGroup) resultButton.getParent()).addView(resultTextView);
        resultButton.setOnClickListener(v -> showResult());
        updateResultButtonState();
    }

    private void initializeViews() {
        titleTextView = findViewById(R.id.titleTextView);
        grammarRecyclerView = findViewById(R.id.grammarRecyclerView);
        grammarRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar(String title) {
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
                    Grammar grammar = grammarList.get(0);
                    exercises = grammar.exercises();
                    adapter = new GrammarAdapter(exercises);
                    grammarRecyclerView.setAdapter(adapter);
                } else {
                    showError("Error loading grammar exercises");
                }
            } else {
                showError("Error loading grammar exercises");
            }
        } catch (Exception e) {
            showError("Error parsing grammar exercises");
        }
    }

    private boolean allExercisesAnswered() {
        if (exercises == null) return false;
        for (GrammarExercise ex : exercises) {
            if (!ex.isAnswered()) return false;
        }
        return true;
    }

    public void updateResultButtonState() {
        int answered = 0;
        int total = exercises != null ? exercises.size() : 0;
        if (exercises != null) {
            for (GrammarExercise ex : exercises) {
                if (ex.isAnswered()) answered++;
            }
        }
        answeredCounter.setText(answered + "/" + total);
        resultButton.setEnabled(answered == total && total > 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateResultButtonState();
    }

    private void showResult() {
        int correct = 0;
        int total = exercises != null ? exercises.size() : 0;
        if (exercises != null) {
            for (GrammarExercise ex : exercises) {
                if (ex.isAnswered() && ex.isCorrect()) correct++;
            }
        }
        int percent = total > 0 ? (100 * correct / total) : 0;
        String result = getString(R.string.correct_answers_count, correct, total) + " (" + percent + "%)";
        resultTextView.setText(result);
        resultTextView.setVisibility(View.VISIBLE);
        resultButton.setVisibility(View.GONE);
        answeredCounter.setVisibility(View.GONE);
    }

    private record Grammar(List<GrammarExercise> exercises) {
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