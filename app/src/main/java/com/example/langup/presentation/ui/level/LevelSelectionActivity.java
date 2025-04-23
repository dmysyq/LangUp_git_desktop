package com.example.langup.presentation.ui.level;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.langup.R;
import com.example.langup.presentation.ui.grammar.GrammarActivity;
import com.example.langup.presentation.ui.questions.QuestionsActivity;
import com.example.langup.presentation.ui.vocabulary.VocabularyActivity;
import com.example.langup.presentation.ui.transcript.TranscriptActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class LevelSelectionActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Initializing activity");
        setContentView(R.layout.activity_level_selection);

        // Get data from intent
        seriesId = getIntent().getStringExtra("series_id");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        int difficulty = getIntent().getIntExtra("difficulty", 0);
        String accent = getIntent().getStringExtra("accent");
        videoUrl = getIntent().getStringExtra("video_url");
        String grammarJson = getIntent().getStringExtra("grammar");
        
        Log.d(TAG, "onCreate: Received data - seriesId: " + seriesId + 
              ", title: " + title + 
              ", description: " + description + 
              ", difficulty: " + difficulty + 
              ", accent: " + accent + 
              ", videoUrl: " + videoUrl);
        Log.d(TAG, "onCreate: Received grammar JSON: " + grammarJson);

        initializeViews();
        setupToolbar();
        setupClickListeners();
        
        // Set text views
        titleTextView.setText(title);
        descriptionTextView.setText(description);
        difficultyTextView.setText(getString(R.string.difficulty_level, String.valueOf(difficulty)));
        accentTextView.setText(getString(R.string.accent_label, accent));
    }

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

        // Set video URL
        if (videoUrl != null && !videoUrl.isEmpty()) {
            videoUrlTextView.setText(videoUrl);
            videoInfoCard.setVisibility(View.VISIBLE);
        } else {
            videoInfoCard.setVisibility(View.GONE);
        }
    }

    private void setupToolbar() {
        Log.d(TAG, "setupToolbar: Setting up toolbar");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.level_selection);
        }
    }

    private void setupClickListeners() {
        Log.d(TAG, "setupClickListeners: Setting up click listeners");
        
        vocabularyButton.setOnClickListener(v -> {
            Log.d(TAG, "Vocabulary button clicked");
            Intent intent = new Intent(this, VocabularyActivity.class);
            intent.putExtra("series_id", seriesId);
            intent.putExtra("title", titleTextView.getText().toString());
            intent.putExtra("vocabulary", getIntent().getStringExtra("vocabulary"));
            startActivity(intent);
        });

        questionsButton.setOnClickListener(v -> {
            Log.d(TAG, "Questions button clicked");
            Intent intent = new Intent(this, QuestionsActivity.class);
            intent.putExtra("series_id", seriesId);
            intent.putExtra("title", titleTextView.getText().toString());
            intent.putExtra("questions", getIntent().getStringExtra("questions"));
            startActivity(intent);
        });

        grammarButton.setOnClickListener(v -> {
            Log.d(TAG, "Grammar button clicked");
            String grammarJson = getIntent().getStringExtra("grammar");
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
            intent.putExtra("transcript", getIntent().getStringExtra("transcript"));
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
} 