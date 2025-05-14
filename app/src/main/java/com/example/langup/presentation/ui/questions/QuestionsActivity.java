package com.example.langup.presentation.ui.questions;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.langup.R;
import com.example.langup.domain.model.SeriesContent.Question;
import com.example.langup.presentation.adapter.QuestionsAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {
    private static final String TAG = "QuestionsActivity";
    
    private TextView titleTextView;
    private RecyclerView questionsRecyclerView;
    private Button resultButton;
    private List<Question> questions;
    private QuestionsAdapter adapter;
    private TextView answeredCounter;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        // Get data from intent
        String title = getIntent().getStringExtra("title");
        String questionsJson = getIntent().getStringExtra("questions");

        Log.d(TAG, "onCreate: Title = " + title);
        Log.d(TAG, "onCreate: Questions JSON = " + questionsJson);

        initializeViews();
        setupToolbar(title);
        setupRecyclerView();
        loadQuestions(questionsJson);
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
        Log.d(TAG, "initializeViews: Initializing views");
        titleTextView = findViewById(R.id.titleTextView);
        questionsRecyclerView = findViewById(R.id.questionsRecyclerView);
    }

    private void setupToolbar(String title) {
        Log.d(TAG, "setupToolbar: Setting up toolbar with title = " + title);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            titleTextView.setText(title);
        }
    }

    private void setupRecyclerView() {
        Log.d(TAG, "setupRecyclerView: Setting up RecyclerView");
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadQuestions(String questionsJson) {
        Log.d(TAG, "loadQuestions: Starting to load questions");
        Log.d(TAG, "loadQuestions: Raw JSON = " + questionsJson);
        
        if (questionsJson != null && !questionsJson.isEmpty()) {
            try {
                Type listType = new TypeToken<List<Question>>(){}.getType();
                questions = new Gson().fromJson(questionsJson, listType);
                adapter = new QuestionsAdapter(questions);
                questionsRecyclerView.setAdapter(adapter);
            } catch (Exception e) {
                Log.e(TAG, "Error parsing questions JSON: " + e.getMessage(), e);
                showError(R.string.error_loading_questions);
            }
        } else {
            Log.e(TAG, "loadQuestions: Questions JSON is null or empty");
            showError(R.string.error_loading_questions);
        }
    }

    private void showError(int messageResId) {
        Log.e(TAG, "showError: Showing error message with resource ID: " + messageResId);
        Snackbar.make(questionsRecyclerView, messageResId, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.d(TAG, "onOptionsItemSelected: Back button pressed");
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Проверка, отвечены ли все вопросы
    private boolean allQuestionsAnswered() {
        if (questions == null) return false;
        for (Question q : questions) {
            if (!q.isAnswered()) return false;
        }
        return true;
    }

    public void updateResultButtonState() {
        int answered = 0;
        int total = questions != null ? questions.size() : 0;
        if (questions != null) {
            for (Question q : questions) {
                if (q.isAnswered()) answered++;
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
        int total = questions != null ? questions.size() : 0;
        if (questions != null) {
            for (Question q : questions) {
                if (q.isAnswered() && q.isCorrect()) correct++;
            }
        }
        int percent = total > 0 ? (100 * correct / total) : 0;
        String result = getString(R.string.correct_answers_count, correct, total) + " (" + percent + "%)";
        resultTextView.setText(result);
        resultTextView.setVisibility(View.VISIBLE);
        resultButton.setVisibility(View.GONE);
        answeredCounter.setVisibility(View.GONE);
    }
} 