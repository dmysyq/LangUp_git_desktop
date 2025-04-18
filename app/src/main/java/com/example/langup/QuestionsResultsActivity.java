package com.example.langup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class QuestionsResultsActivity extends AppCompatActivity {
    private static final String EXTRA_QUESTIONS = "extra_questions";
    private static final String EXTRA_RESULTS = "extra_results";
    private static final String EXTRA_USER_ANSWERS = "extra_user_answers";

    private TextView scoreTextView;
    private RecyclerView resultsRecyclerView;
    private ResultsAdapter adapter;

    public static Intent newIntent(Context context, String questionsJson, String resultsJson, String userAnswersJson) {
        Intent intent = new Intent(context, QuestionsResultsActivity.class);
        intent.putExtra(EXTRA_QUESTIONS, questionsJson);
        intent.putExtra(EXTRA_RESULTS, resultsJson);
        intent.putExtra(EXTRA_USER_ANSWERS, userAnswersJson);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_results);

        initializeViews();
        setupToolbar();
        loadResults();
    }

    private void initializeViews() {
        scoreTextView = findViewById(R.id.scoreTextView);
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ResultsAdapter();
        resultsRecyclerView.setAdapter(adapter);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadResults() {
        try {
            String questionsJson = getIntent().getStringExtra(EXTRA_QUESTIONS);
            String resultsJson = getIntent().getStringExtra(EXTRA_RESULTS);
            String userAnswersJson = getIntent().getStringExtra(EXTRA_USER_ANSWERS);

            JSONArray questions = new JSONArray(questionsJson);
            JSONArray results = new JSONArray(resultsJson);
            JSONArray userAnswers = new JSONArray(userAnswersJson);

            int correctCount = 0;
            List<ResultItem> resultItems = new ArrayList<>();

            for (int i = 0; i < questions.length(); i++) {
                JSONObject question = questions.getJSONObject(i);
                boolean isCorrect = results.getBoolean(i);
                if (isCorrect) correctCount++;

                String questionText = question.getString("question");
                String type = question.getString("type");
                JSONArray options = question.getJSONArray("options");

                String userAnswer = formatAnswer(userAnswers.getJSONArray(i), options);
                String correctAnswer = formatAnswer(
                    "single_choice".equals(type) 
                        ? new JSONArray().put(question.getInt("correctAnswer"))
                        : question.getJSONArray("correctAnswers"),
                    options
                );

                resultItems.add(new ResultItem(
                    i + 1,
                    questionText,
                    userAnswer,
                    correctAnswer,
                    isCorrect
                ));
            }

            scoreTextView.setText(getString(R.string.correct_answers, correctCount, questions.length()));
            adapter.setItems(resultItems);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String formatAnswer(JSONArray indices, JSONArray options) throws JSONException {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < indices.length(); i++) {
            if (i > 0) result.append(", ");
            result.append(options.getString(indices.getInt(i)));
        }
        return result.toString();
    }

    private static class ResultItem {
        final int number;
        final String question;
        final String userAnswer;
        final String correctAnswer;
        final boolean isCorrect;

        ResultItem(int number, String question, String userAnswer, String correctAnswer, boolean isCorrect) {
            this.number = number;
            this.question = question;
            this.userAnswer = userAnswer;
            this.correctAnswer = correctAnswer;
            this.isCorrect = isCorrect;
        }
    }

    private class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {
        private List<ResultItem> items = new ArrayList<>();

        void setItems(List<ResultItem> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(getLayoutInflater().inflate(R.layout.item_question_result, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ResultItem item = items.get(position);
            holder.questionNumberTextView.setText(getString(R.string.question_number, item.number));
            holder.questionTextView.setText(item.question);
            holder.userAnswerTextView.setText(item.userAnswer);
            holder.userAnswerTextView.setTextColor(item.isCorrect ? 0xFF4CAF50 : 0xFFF44336);
            holder.correctAnswerTextView.setText(item.correctAnswer);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView questionNumberTextView;
            final TextView questionTextView;
            final TextView userAnswerTextView;
            final TextView correctAnswerTextView;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                questionNumberTextView = itemView.findViewById(R.id.questionNumberTextView);
                questionTextView = itemView.findViewById(R.id.questionTextView);
                userAnswerTextView = itemView.findViewById(R.id.userAnswerTextView);
                correctAnswerTextView = itemView.findViewById(R.id.correctAnswerTextView);
            }
        }
    }
} 