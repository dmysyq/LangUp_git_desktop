package com.example.langup.activities;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.langup.R;
import com.example.langup.adapters.GrammarSentenceAdapter;
import com.example.langup.models.GrammarExercise;
import com.example.langup.models.GrammarSentence;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import android.widget.TextView;
import org.json.JSONException;

public class GrammarActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChipGroup availableWordsChipGroup;
    private MaterialButton checkButton;
    private MaterialButton resetButton;
    private GrammarSentenceAdapter adapter;
    private GrammarExercise exercise;
    private String episodeId;
    private Toolbar toolbar;
    private TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar);

        episodeId = getIntent().getStringExtra("episodeId");
        if (episodeId == null) {
            Toast.makeText(this, R.string.error_loading_episode, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        loadExercise();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.grammar_exercise);

        recyclerView = findViewById(R.id.recyclerView);
        availableWordsChipGroup = findViewById(R.id.availableWordsChipGroup);
        checkButton = findViewById(R.id.checkButton);
        resetButton = findViewById(R.id.resetButton);
        descriptionTextView = findViewById(R.id.descriptionText);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GrammarSentenceAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        checkButton.setOnClickListener(v -> checkAnswers());
        resetButton.setOnClickListener(v -> resetExercise());
    }

    private void loadExercise() {
        try {
            InputStream is = getAssets().open("grammar_exercises.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONObject jsonObject = new JSONObject(json);
            JSONObject exerciseObj = jsonObject.getJSONObject("exercise");
            String description = exerciseObj.getString("description");

            exercise = new GrammarExercise(exerciseObj.getString("id"), description);

            JSONArray sentencesArray = exerciseObj.getJSONArray("sentences");
            for (int i = 0; i < sentencesArray.length(); i++) {
                JSONObject sentenceObj = sentencesArray.getJSONObject(i);
                GrammarSentence sentence = new GrammarSentence(
                    sentenceObj.getString("id"),
                    sentenceObj.getString("sentence"),
                    sentenceObj.getString("correctAnswer"),
                    sentenceObj.getString("explanation")
                );
                exercise.addSentence(sentence);
            }

            adapter.setSentences(exercise.getSentences());

            JSONArray wordsArray = exerciseObj.getJSONArray("availableWords");
            List<String> availableWords = new ArrayList<>();
            for (int i = 0; i < wordsArray.length(); i++) {
                availableWords.add(wordsArray.getString(i));
            }
            setupAvailableWords(availableWords);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.error_loading_episode, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupAvailableWords(List<String> words) {
        availableWordsChipGroup.removeAllViews();
        for (String word : words) {
            Chip chip = new Chip(this);
            chip.setText(word);
            chip.setClickable(true);
            chip.setCheckable(true);
            chip.setOnClickListener(v -> {
                String selectedWord = ((Chip) v).getText().toString();
                adapter.setSelectedWord(selectedWord);
            });
            availableWordsChipGroup.addView(chip);
        }
    }

    private void checkAnswers() {
        int correctAnswers = exercise.getCorrectAnswers();
        int totalQuestions = exercise.getTotalQuestions();
        showResultsDialog(correctAnswers, totalQuestions);
    }

    private void resetExercise() {
        adapter.resetFeedback();
    }

    private void showResultsDialog(int correctAnswers, int totalQuestions) {
        double percentage = (double) correctAnswers / totalQuestions * 100;
        String message;
        if (percentage >= 80) {
            message = getString(R.string.excellent_job);
        } else if (percentage >= 60) {
            message = getString(R.string.good_job);
        } else {
            message = getString(R.string.keep_practicing);
        }

        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(getString(R.string.results))
            .setMessage(String.format("%s\n%d/%d", message, correctAnswers, totalQuestions))
            .setPositiveButton(getString(R.string.ok), (dialog, which) -> finish())
            .show();
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