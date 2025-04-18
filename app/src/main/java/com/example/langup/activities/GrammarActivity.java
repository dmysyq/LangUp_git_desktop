package com.example.langup.activities;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.langup.R;
import com.example.langup.adapters.GrammarSentenceAdapter;
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

public class GrammarActivity extends AppCompatActivity implements GrammarSentenceAdapter.OnAnswerChangedListener {
    private RecyclerView sentencesRecyclerView;
    private ChipGroup availableWordsChipGroup;
    private MaterialButton checkAnswersButton;
    private MaterialButton resetButton;
    private GrammarSentenceAdapter adapter;
    private List<GrammarSentence> sentences;
    private Map<Integer, String> userAnswers;
    private String episodeId;
    private Toolbar toolbar;

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
        loadGrammarExercise();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.grammar_exercise);

        sentencesRecyclerView = findViewById(R.id.sentencesRecyclerView);
        availableWordsChipGroup = findViewById(R.id.availableWordsChipGroup);
        checkAnswersButton = findViewById(R.id.checkAnswersButton);
        resetButton = findViewById(R.id.resetButton);

        sentencesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAnswers = new HashMap<>();

        checkAnswersButton.setOnClickListener(v -> checkAnswers());
        resetButton.setOnClickListener(v -> resetExercise());
    }

    private void loadGrammarExercise() {
        try {
            InputStream is = getAssets().open("content.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONObject obj = new JSONObject(json);
            JSONArray seriesArray = obj.getJSONArray("series");
            
            for (int i = 0; i < seriesArray.length(); i++) {
                JSONObject series = seriesArray.getJSONObject(i);
                JSONArray episodes = series.getJSONArray("episodes");
                
                for (int j = 0; j < episodes.length(); j++) {
                    JSONObject episode = episodes.getJSONObject(j);
                    if (episode.getString("id").equals(episodeId)) {
                        JSONObject grammar = episode.getJSONObject("grammar");
                        setupGrammarExercise(grammar);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.error_loading_episode, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupGrammarExercise(JSONObject grammar) throws Exception {
        // Setup description
        String description = grammar.optString("description", getString(R.string.fill_in_the_blanks));
        findViewById(R.id.descriptionText).setVisibility(View.VISIBLE);
        ((android.widget.TextView) findViewById(R.id.descriptionText)).setText(description);

        // Setup available words
        availableWordsChipGroup.removeAllViews();
        JSONArray words = grammar.getJSONArray("availableWords");
        for (int i = 0; i < words.length(); i++) {
            Chip chip = new Chip(this);
            chip.setText(words.getString(i));
            chip.setClickable(true);
            chip.setCheckable(false);
            chip.setOnClickListener(v -> {
                String word = ((Chip) v).getText().toString();
                // Find the first empty answer field
                for (int j = 0; j < sentences.size(); j++) {
                    if (!userAnswers.containsKey(j) || userAnswers.get(j).isEmpty()) {
                        userAnswers.put(j, word);
                        adapter.notifyItemChanged(j);
                        break;
                    }
                }
            });
            availableWordsChipGroup.addView(chip);
        }

        // Setup sentences
        sentences = new ArrayList<>();
        JSONArray sentencesArray = grammar.getJSONArray("sentences");
        for (int i = 0; i < sentencesArray.length(); i++) {
            JSONObject sentenceObj = sentencesArray.getJSONObject(i);
            List<String> parts = new ArrayList<>();
            JSONArray partsArray = sentenceObj.getJSONArray("parts");
            for (int j = 0; j < partsArray.length(); j++) {
                parts.add(partsArray.getString(j));
            }
            
            List<String> answers = new ArrayList<>();
            JSONArray answersArray = sentenceObj.getJSONArray("answers");
            for (int j = 0; j < answersArray.length(); j++) {
                answers.add(answersArray.getString(j));
            }
            
            sentences.add(new GrammarSentence(sentenceObj.getString("id"), parts, answers));
        }

        adapter = new GrammarSentenceAdapter(sentences, this);
        sentencesRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onAnswerChanged(int position, String answer) {
        userAnswers.put(position, answer);
    }

    private void checkAnswers() {
        int correctCount = 0;
        for (int i = 0; i < sentences.size(); i++) {
            String userAnswer = userAnswers.get(i);
            List<String> correctAnswers = sentences.get(i).getAnswers();
            boolean isCorrect = userAnswer != null && correctAnswers.contains(userAnswer);
            if (isCorrect) {
                correctCount++;
            }
            adapter.showFeedback(i, isCorrect);
        }

        String message = getString(R.string.correct_answers_count, correctCount, sentences.size());
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        checkAnswersButton.setEnabled(false);
        resetButton.setVisibility(View.VISIBLE);
    }

    private void resetExercise() {
        userAnswers.clear();
        adapter.resetFeedback();
        checkAnswersButton.setEnabled(true);
        resetButton.setVisibility(View.GONE);
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