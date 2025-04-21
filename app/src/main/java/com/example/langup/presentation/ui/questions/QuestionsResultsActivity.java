package com.example.langup.presentation.ui.questions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langup.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.example.langup.domain.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionsResultsActivity extends AppCompatActivity {
    private static final String TAG = "QuestionsResultsActivity";
    private static final String EXTRA_QUESTIONS = "questions";
    private static final String EXTRA_RESULTS = "results";
    private static final String EXTRA_USER_ANSWERS = "userAnswers";

    private RecyclerView resultsRecyclerView;
    private TextView scoreTextView;
    private QuestionsResultsAdapter resultsAdapter;

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

        resultsRecyclerView = findViewById(R.id.resultsRecyclerView);
        scoreTextView = findViewById(R.id.scoreTextView);

        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        resultsAdapter = new QuestionsResultsAdapter();
        resultsRecyclerView.setAdapter(resultsAdapter);

        setupToolbar();
        loadResults();
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

            if (questionsJson == null || resultsJson == null || userAnswersJson == null) {
                Log.e(TAG, "Missing required JSON data");
                finish();
                return;
            }

            JSONArray questionsArray = new JSONArray(questionsJson);
            JSONArray resultsArray = new JSONArray(resultsJson);
            JSONArray userAnswersArray = new JSONArray(userAnswersJson);

            List<QuestionResult> results = new ArrayList<>();
            int correctAnswers = 0;

            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject questionObj = questionsArray.getJSONObject(i);
                JSONObject resultObj = resultsArray.getJSONObject(i);
                String userAnswer = userAnswersArray.getString(i);

                Question question = new Question();
                question.setId(questionObj.getString("id"));
                question.setType(questionObj.getString("type"));
                question.setQuestion(questionObj.getString("question"));
                
                JSONArray optionsArray = questionObj.getJSONArray("options");
                List<String> options = new ArrayList<>();
                for (int j = 0; j < optionsArray.length(); j++) {
                    options.add(optionsArray.getString(j));
                }
                question.setOptions(options);
                
                Object correctAnswer = questionObj.get("correctAnswer");
                if (correctAnswer instanceof Integer) {
                    question.setCorrectAnswer(correctAnswer);
                } else if (correctAnswer instanceof JSONArray) {
                    JSONArray correctAnswersArray = (JSONArray) correctAnswer;
                    List<Integer> correctAnswerIndices = new ArrayList<>();
                    for (int j = 0; j < correctAnswersArray.length(); j++) {
                        correctAnswerIndices.add(correctAnswersArray.getInt(j));
                    }
                    question.setCorrectAnswer(correctAnswerIndices);
                }

                boolean isCorrect = resultObj.getBoolean("isCorrect");
                if (isCorrect) {
                    correctAnswers++;
                }

                String correctAnswerText = question.isSingleChoice() 
                    ? options.get((Integer) question.getCorrectAnswer())
                    : question.getCorrectAnswers().stream()
                        .map(index -> options.get(index))
                        .collect(Collectors.joining(", "));

                results.add(new QuestionResult(
                    i + 1,
                    question.getQuestion(),
                    userAnswer,
                    correctAnswerText,
                    isCorrect
                ));
            }

            int totalQuestions = questionsArray.length();
            int score = (correctAnswers * 100) / totalQuestions;
            scoreTextView.setText(getString(R.string.score_format, score, totalQuestions));
            resultsAdapter.setResults(results);

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing results JSON", e);
            finish();
        }
    }

    private static class QuestionResult {
        private final int questionNumber;
        private final String question;
        private final String userAnswer;
        private final String correctAnswer;
        private final boolean isCorrect;

        QuestionResult(int questionNumber, String question, String userAnswer, String correctAnswer, boolean isCorrect) {
            this.questionNumber = questionNumber;
            this.question = question;
            this.userAnswer = userAnswer;
            this.correctAnswer = correctAnswer;
            this.isCorrect = isCorrect;
        }

        public int getQuestionNumber() { return questionNumber; }
        public String getQuestion() { return question; }
        public String getUserAnswer() { return userAnswer; }
        public String getCorrectAnswer() { return correctAnswer; }
        public boolean isCorrect() { return isCorrect; }
    }

    private class QuestionsResultsAdapter extends RecyclerView.Adapter<QuestionsResultsAdapter.ViewHolder> {
        private List<QuestionResult> results = new ArrayList<>();

        void setResults(List<QuestionResult> results) {
            this.results = results;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(getLayoutInflater().inflate(R.layout.item_question_result, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            QuestionResult result = results.get(position);
            holder.questionNumberTextView.setText(getString(R.string.question_number, result.getQuestionNumber()));
            holder.questionTextView.setText(result.getQuestion());
            holder.userAnswerTextView.setText(result.getUserAnswer());
            holder.userAnswerTextView.setTextColor(result.isCorrect() ? 0xFF4CAF50 : 0xFFF44336);
            holder.correctAnswerTextView.setText(result.getCorrectAnswer());
        }

        @Override
        public int getItemCount() {
            return results.size();
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