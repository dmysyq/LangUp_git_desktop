package com.example.langup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GrammarActivity extends AppCompatActivity {
    private TextView titleTextView;
    private TextView progressTextView;
    private TextView explanationTextView;
    private TextView feedbackTextView;
    private ChipGroup optionsChipGroup;
    private LinearLayout sentenceContainer;
    private MaterialButton resetButton;
    private MaterialButton actionButton;

    private List<JSONObject> exercises;
    private int currentExerciseIndex = 0;
    private boolean isCheckingAnswer = false;
    private List<Boolean> results;
    private EditText currentInput;

    private static final String EXTRA_SERIES_ID = "extra_series_id";
    private static final String EXTRA_EPISODE_ID = "extra_episode_id";

    public static Intent newIntent(Context context, String seriesId, String episodeId) {
        Intent intent = new Intent(context, GrammarActivity.class);
        intent.putExtra(EXTRA_SERIES_ID, seriesId);
        intent.putExtra(EXTRA_EPISODE_ID, episodeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar);

        String seriesId = getIntent().getStringExtra(EXTRA_SERIES_ID);
        String episodeId = getIntent().getStringExtra(EXTRA_EPISODE_ID);

        initializeViews();
        setupToolbar();
        loadExercises(seriesId, episodeId);
        results = new ArrayList<>();
        showExercise(currentExerciseIndex);
    }

    private void initializeViews() {
        titleTextView = findViewById(R.id.titleTextView);
        progressTextView = findViewById(R.id.progressTextView);
        explanationTextView = findViewById(R.id.explanationTextView);
        feedbackTextView = findViewById(R.id.feedbackTextView);
        optionsChipGroup = findViewById(R.id.optionsChipGroup);
        sentenceContainer = findViewById(R.id.sentenceContainer);
        resetButton = findViewById(R.id.resetButton);
        actionButton = findViewById(R.id.actionButton);

        resetButton.setOnClickListener(v -> resetExercise());
        actionButton.setOnClickListener(v -> onActionButtonClick());
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleTextView.setText(R.string.grammar);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadExercises(String seriesId, String episodeId) {
        try {
            String jsonFileName = seriesId + "_" + episodeId + ".json";
            String jsonString = loadJSONFromAsset(jsonFileName);
            JSONObject json = new JSONObject(jsonString);
            JSONArray grammarArray = json.getJSONArray("grammar");
            
            exercises = new ArrayList<>();
            for (int i = 0; i < grammarArray.length(); i++) {
                exercises.add(grammarArray.getJSONObject(i));
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            // Handle error
        }
    }

    private String loadJSONFromAsset(String fileName) throws IOException {
        InputStream is = getAssets().open(fileName);
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        is.close();
        return new String(buffer, StandardCharsets.UTF_8);
    }

    private void showExercise(int index) {
        try {
            JSONObject exercise = exercises.get(index);
            String explanation = exercise.getString("explanation");
            String sentence = exercise.getString("sentence");
            JSONArray options = exercise.getJSONArray("options");

            progressTextView.setText(String.format("%d/%d", index + 1, exercises.size()));
            explanationTextView.setText(explanation);
            
            setupOptions(options);
            setupSentence(sentence);

            actionButton.setText(R.string.check_answer);
            actionButton.setEnabled(false);
            isCheckingAnswer = false;
            feedbackTextView.setVisibility(View.GONE);
            resetButton.setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupOptions(JSONArray options) throws JSONException {
        optionsChipGroup.removeAllViews();
        
        for (int i = 0; i < options.length(); i++) {
            String option = options.getString(i);
            if (!option.isEmpty()) {
                Chip chip = new Chip(this);
                chip.setText(option);
                chip.setCheckable(true);
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked && currentInput != null) {
                        currentInput.setText(buttonView.getText());
                        actionButton.setEnabled(true);
                    }
                });
                optionsChipGroup.addView(chip);
            }
        }
    }

    private void setupSentence(String sentence) {
        sentenceContainer.removeAllViews();
        String[] parts = sentence.split("___");
        
        for (int i = 0; i < parts.length; i++) {
            // Add text part
            if (!parts[i].isEmpty()) {
                TextView textView = new TextView(this);
                textView.setText(parts[i]);
                textView.setTextSize(16);
                sentenceContainer.addView(textView);
            }
            
            // Add input field if not last part
            if (i < parts.length - 1) {
                EditText editText = new EditText(this);
                editText.setBackground(ContextCompat.getDrawable(this, R.drawable.grammar_input_background));
                editText.setMinWidth(100);
                editText.setMaxWidth(200);
                editText.setTextSize(16);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        actionButton.setEnabled(!s.toString().trim().isEmpty());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });
                
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(8, 0, 8, 0);
                editText.setLayoutParams(params);
                
                sentenceContainer.addView(editText);
                currentInput = editText;
            }
        }
    }

    private void resetExercise() {
        optionsChipGroup.clearCheck();
        if (currentInput != null) {
            currentInput.setText("");
        }
        actionButton.setEnabled(false);
        feedbackTextView.setVisibility(View.GONE);
    }

    private void onActionButtonClick() {
        if (!isCheckingAnswer) {
            checkAnswer();
        } else if (currentExerciseIndex < exercises.size() - 1) {
            animateToNextExercise();
        } else {
            showResults();
        }
    }

    private void checkAnswer() {
        try {
            JSONObject exercise = exercises.get(currentExerciseIndex);
            String correctAnswer = exercise.getString("correctAnswer");
            String userAnswer = currentInput.getText().toString().trim();
            boolean isCorrect = correctAnswer.equalsIgnoreCase(userAnswer);

            results.add(isCorrect);
            currentInput.setEnabled(false);
            optionsChipGroup.setEnabled(false);

            feedbackTextView.setText(isCorrect ? R.string.correct_answer : R.string.incorrect_answer);
            feedbackTextView.setTextColor(ContextCompat.getColor(this, 
                isCorrect ? R.color.correct_answer : R.color.incorrect_answer));
            feedbackTextView.setVisibility(View.VISIBLE);

            if (!isCorrect) {
                currentInput.setText(correctAnswer);
            }

            actionButton.setText(currentExerciseIndex < exercises.size() - 1 ? 
                    R.string.next : R.string.show_results);
            isCheckingAnswer = true;
            resetButton.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void animateToNextExercise() {
        ObjectAnimator slideOut = ObjectAnimator.ofFloat(sentenceContainer, "translationX", 0f, -sentenceContainer.getWidth());
        slideOut.setDuration(300);
        slideOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentExerciseIndex++;
                showExercise(currentExerciseIndex);
                sentenceContainer.setTranslationX(sentenceContainer.getWidth());
                ObjectAnimator slideIn = ObjectAnimator.ofFloat(sentenceContainer, "translationX", sentenceContainer.getWidth(), 0f);
                slideIn.setDuration(300);
                slideIn.start();
            }
        });
        slideOut.start();
    }

    private void showResults() {
        try {
            // Convert exercises to JSON array
            JSONArray exercisesArray = new JSONArray();
            for (JSONObject exercise : exercises) {
                exercisesArray.put(exercise);
            }

            // Convert results to JSON array
            JSONArray resultsArray = new JSONArray();
            for (Boolean result : results) {
                resultsArray.put(result);
            }

            // Start results activity
            startActivity(GrammarResultsActivity.newIntent(
                this,
                exercisesArray.toString(),
                resultsArray.toString()
            ));
            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
} 