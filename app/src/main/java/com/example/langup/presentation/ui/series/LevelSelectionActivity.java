package com.example.langup.presentation.ui.series;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.langup.presentation.ui.grammar.GrammarActivity;
import com.example.langup.presentation.ui.questions.QuestionsActivity;
import com.example.langup.R;
import com.example.langup.presentation.ui.vocabulary.VocabularyActivity;
import com.example.langup.domain.model.Series;
import com.example.langup.data.repository.ContentManager;

public class LevelSelectionActivity extends AppCompatActivity {
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView sourceTextView;
    private Button youtubeButton;
    private Button questionsButton;
    private Button vocabularyButton;
    private Button grammarButton;
    private Button buildingButton;
    private Toolbar toolbar;
    private String seriesId;

    public static Intent newIntent(android.content.Context context, String seriesId) {
        Intent intent = new Intent(context, LevelSelectionActivity.class);
        intent.putExtra("seriesId", seriesId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);

        seriesId = getIntent().getStringExtra("seriesId");
        if (seriesId == null) {
            finish();
            return;
        }

        initializeViews();
        loadSeriesData();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.select_level);

        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        sourceTextView = findViewById(R.id.sourceTextView);
        youtubeButton = findViewById(R.id.youtubeButton);
        questionsButton = findViewById(R.id.questionsButton);
        vocabularyButton = findViewById(R.id.vocabularyButton);
        grammarButton = findViewById(R.id.grammarButton);
        buildingButton = findViewById(R.id.buildingButton);

        youtubeButton.setOnClickListener(v -> openYoutubeVideo());
        questionsButton.setOnClickListener(v -> startQuestionsActivity());
        vocabularyButton.setOnClickListener(v -> startVocabularyActivity());
        grammarButton.setOnClickListener(v -> startGrammarActivity());
    }

    private void loadSeriesData() {
        ContentManager contentManager = new ContentManager(this);
        contentManager.getSeriesById(seriesId).ifPresent(series -> {
            titleTextView.setText(series.getTitle());
            descriptionTextView.setText(series.getDescription());
            sourceTextView.setText(series.getSource());
            
            // Store the series ID for later use
            contentManager.setSelectedSeries(seriesId);
            
            // Set YouTube URL
            if (series.getEpisodes() != null && !series.getEpisodes().isEmpty()) {
                String youtubeUrl = series.getEpisodes().get(0).getYoutubeUrl();
                if (youtubeUrl != null && !youtubeUrl.isEmpty()) {
                    youtubeButton.setVisibility(View.VISIBLE);
                    youtubeButton.setTag(youtubeUrl);
                } else {
                    youtubeButton.setVisibility(View.GONE);
                }
            } else {
                youtubeButton.setVisibility(View.GONE);
            }
            
            // Enable/disable level buttons based on available content
            updateLevelButtons(series);
        });
    }

    private void updateLevelButtons(Series series) {
        // This method would check if the series has content for each level
        // and enable/disable buttons accordingly
        // For now, we'll just enable all buttons
        questionsButton.setEnabled(true);
        vocabularyButton.setEnabled(true);
        grammarButton.setEnabled(true);
        buildingButton.setEnabled(true);
    }

    private void openYoutubeVideo() {
        String youtubeUrl = (String) youtubeButton.getTag();
        if (youtubeUrl != null && !youtubeUrl.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl));
            startActivity(intent);
        }
    }

    private void startQuestionsActivity() {
        // Start Questions activity with the selected series and first episode
        Intent intent = QuestionsActivity.newIntent(this, seriesId, "s1e01");
        startActivity(intent);
    }

    private void startVocabularyActivity() {
        // Start Vocabulary activity with the selected series
        Intent intent = new Intent(this, VocabularyActivity.class);
        startActivity(intent);
    }

    private void startGrammarActivity() {
        // Start Grammar activity with the selected series
        Intent intent = new Intent(this, GrammarActivity.class);
        startActivity(intent);
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