package com.example.langup.presentation.ui.questions;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
    private QuestionsAdapter adapter;

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
                List<Question> questions = new Gson().fromJson(questionsJson, listType);
                Log.d(TAG, "loadQuestions: Successfully parsed " + questions.size() + " questions");
                
                // Log each question details
                for (Question question : questions) {
                    Log.d(TAG, String.format("Question details:\n" +
                        "  id: %s\n" +
                        "  type: %s\n" +
                        "  question: %s\n" +
                        "  options: %s\n" +
                        "  correctAnswer: %s\n" +
                        "  correctAnswers: %s",
                        question.getId(),
                        question.getType(),
                        question.getQuestion(),
                        question.getOptions(),
                        question.getCorrectAnswer(),
                        question.getCorrectAnswers()));
                }
                
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
} 