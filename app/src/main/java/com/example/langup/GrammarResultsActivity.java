package com.example.langup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GrammarResultsActivity extends AppCompatActivity {
    private static final String EXTRA_EXERCISES = "extra_exercises";
    private static final String EXTRA_RESULTS = "extra_results";

    private TextView titleTextView;
    private TextView scoreTextView;
    private RecyclerView resultsRecyclerView;

    public static Intent newIntent(Context context, String exercises, String results) {
        Intent intent = new Intent(context, GrammarResultsActivity.class);
        intent.putExtra(EXTRA_EXERCISES, exercises);
        intent.putExtra(EXTRA_RESULTS, results);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_results);

        initializeViews();
        setupToolbar();
        displayResults();
    }

    private void initializeViews() {
        titleTextView = findViewById(R.id.titleTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleTextView.setText(R.string.results);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayResults() {
        try {
            String exercisesJson = getIntent().getStringExtra(EXTRA_EXERCISES);
            String resultsJson = getIntent().getStringExtra(EXTRA_RESULTS);

            JSONArray exercisesArray = new JSONArray(exercisesJson);
            JSONArray resultsArray = new JSONArray(resultsJson);

            List<ResultItem> resultItems = new ArrayList<>();
            int correctAnswers = 0;

            for (int i = 0; i < exercisesArray.length(); i++) {
                JSONObject exercise = exercisesArray.getJSONObject(i);
                boolean isCorrect = resultsArray.getBoolean(i);
                if (isCorrect) {
                    correctAnswers++;
                }

                resultItems.add(new ResultItem(
                    i + 1,
                    exercise.getString("sentence"),
                    exercise.getString("correctAnswer"),
                    isCorrect
                ));
            }

            // Display score
            int totalQuestions = exercisesArray.length();
            int percentage = (correctAnswers * 100) / totalQuestions;
            scoreTextView.setText(getString(R.string.score_format, 
                correctAnswers, totalQuestions, percentage));

            // Display results list
            ResultsAdapter adapter = new ResultsAdapter(resultItems);
            resultsRecyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static class ResultItem {
        final int number;
        final String sentence;
        final String correctAnswer;
        final boolean isCorrect;

        ResultItem(int number, String sentence, String correctAnswer, boolean isCorrect) {
            this.number = number;
            this.sentence = sentence;
            this.correctAnswer = correctAnswer;
            this.isCorrect = isCorrect;
        }
    }

    private class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {
        private final List<ResultItem> items;

        ResultsAdapter(List<ResultItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            return new ViewHolder(getLayoutInflater().inflate(
                R.layout.item_grammar_result, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ResultItem item = items.get(position);
            
            // Replace blank with correct answer
            String formattedSentence = item.sentence.replace("___", item.correctAnswer);
            
            holder.numberTextView.setText(String.valueOf(item.number));
            holder.sentenceTextView.setText(formattedSentence);
            holder.resultTextView.setText(item.isCorrect ? 
                R.string.correct : R.string.incorrect);
            holder.resultTextView.setTextColor(getColor(item.isCorrect ? 
                R.color.correct_answer : R.color.incorrect_answer));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView numberTextView;
            final TextView sentenceTextView;
            final TextView resultTextView;

            ViewHolder(@NonNull android.view.View itemView) {
                super(itemView);
                numberTextView = itemView.findViewById(R.id.numberTextView);
                sentenceTextView = itemView.findViewById(R.id.sentenceTextView);
                resultTextView = itemView.findViewById(R.id.resultTextView);
            }
        }
    }
} 