package com.example.langup.presentation.ui.level;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.example.langup.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.example.langup.presentation.base.BaseActivity;
import com.example.langup.presentation.ui.vocabulary.VocabularyActivity;
import com.example.langup.presentation.ui.grammar.GrammarActivity;
import com.example.langup.presentation.ui.questions.QuestionsActivity;
import com.example.langup.presentation.ui.transcript.TranscriptActivity;

public class LevelSelectionActivity extends BaseActivity {
    private static final String TAG = "LevelSelectionActivity";
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView difficultyTextView;
    private TextView accentTextView;
    private TextView videoUrlTextView;
    private MaterialCardView videoInfoCard;
    private MaterialButton vocabularyButton;
    private MaterialButton questionsButton;
    private MaterialButton grammarButton;
    private MaterialButton transcriptButton;

    private String seriesId;
    private String videoUrl;

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Initializing activity");
        setContentView(R.layout.activity_level_selection);

        // Get data from intent using old keys and types
        seriesId = getIntent().getStringExtra("series_id");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        int difficulty = getIntent().getIntExtra("difficulty", 0);
        String accent = getIntent().getStringExtra("accent");
        videoUrl = getIntent().getStringExtra("video_url");
        String grammarJson = getIntent().getStringExtra("grammar");
        String vocabularyJson = getIntent().getStringExtra("vocabulary");
        String questionsJson = getIntent().getStringExtra("questions");
        String transcript = getIntent().getStringExtra("transcript");

        Log.d(TAG, "onCreate: Received data - seriesId: " + seriesId +
                ", title: " + title +
                ", description: " + description +
                ", difficulty: " + difficulty +
                ", accent: " + accent +
                ", videoUrl: " + videoUrl);
        Log.d(TAG, "onCreate: Received grammar JSON: " + grammarJson);

        initializeViews();
        setupToolbar(title);
        setupClickListeners(title, vocabularyJson, grammarJson, questionsJson, transcript);

        // Set text views (always show fields, even if empty)
        titleTextView.setText(title != null ? title : "");
        descriptionTextView.setText(description != null ? description : "");
        difficultyTextView.setText(getString(R.string.difficulty_level, String.valueOf(difficulty)));
        accentTextView.setText(getString(R.string.accent_label, accent != null ? accent : ""));
        if (videoUrl != null && !videoUrl.isEmpty()) {
            videoUrlTextView.setText(videoUrl);
            videoInfoCard.setVisibility(View.VISIBLE);
        } else {
            videoInfoCard.setVisibility(View.GONE);
        }
    }

    @SuppressLint("WrongViewCast")
    private void initializeViews() {
        Log.d(TAG, "initializeViews: Setting up views");
        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        difficultyTextView = findViewById(R.id.difficultyTextView);
        accentTextView = findViewById(R.id.accentTextView);
        videoUrlTextView = findViewById(R.id.videoUrlTextView);
        videoInfoCard = findViewById(R.id.videoInfoCard);
        vocabularyButton = findViewById(R.id.vocabularyButton);
        questionsButton = findViewById(R.id.questionsButton);
        grammarButton = findViewById(R.id.grammarButton);
        transcriptButton = findViewById(R.id.transcriptButton);
    }

    private void setupToolbar(String title) {
        Log.d(TAG, "setupToolbar: Setting up toolbar");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.level_selection);
        }
    }

    private void setupClickListeners(String title, String vocabularyJson, String grammarJson, String questionsJson, String transcript) {
        Log.d(TAG, "setupClickListeners: Setting up click listeners");

        vocabularyButton.setOnClickListener(v -> {
            Log.d(TAG, "Vocabulary button clicked");
            Intent intent = new Intent(this, VocabularyActivity.class);
            intent.putExtra("series_id", seriesId);
            intent.putExtra("title", titleTextView.getText().toString());
            intent.putExtra("vocabulary", vocabularyJson);
            startActivity(intent);
        });

        questionsButton.setOnClickListener(v -> {
            Log.d(TAG, "Questions button clicked");
            Intent intent = new Intent(this, QuestionsActivity.class);
            intent.putExtra("series_id", seriesId);
            intent.putExtra("title", titleTextView.getText().toString());
            intent.putExtra("questions", questionsJson);
            startActivity(intent);
        });

        grammarButton.setOnClickListener(v -> {
            Log.d(TAG, "Grammar button clicked");
            Log.d(TAG, "Grammar data being passed: " + grammarJson);
            Intent intent = new Intent(this, GrammarActivity.class);
            intent.putExtra("series_id", seriesId);
            intent.putExtra("title", titleTextView.getText().toString());
            intent.putExtra("grammar", grammarJson);
            startActivity(intent);
        });

        transcriptButton.setOnClickListener(v -> {
            Log.d(TAG, "Transcript button clicked");
            Intent intent = new Intent(this, TranscriptActivity.class);
            intent.putExtra("series_id", seriesId);
            intent.putExtra("title", titleTextView.getText().toString());
            intent.putExtra("transcript", transcript);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "onSupportNavigateUp: Navigating back");
        onBackPressed();
        return true;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_level_selection;
    }
} 