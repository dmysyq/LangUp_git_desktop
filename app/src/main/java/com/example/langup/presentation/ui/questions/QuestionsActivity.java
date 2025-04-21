package com.example.langup.presentation.ui.questions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langup.R;
import com.example.langup.data.repository.ContentManager;
import com.example.langup.domain.model.Question;
import com.example.langup.domain.model.Series;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuestionsActivity extends AppCompatActivity {
    private static final String TAG = "QuestionsActivity";
    private static final String EXTRA_SERIES_ID = "series_id";
    private static final String EXTRA_EPISODE_ID = "episode_id";

    private ContentManager contentManager;
    private Optional<Series> currentSeries;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private List<String> userAnswers = new ArrayList<>();

    private TextView questionTextView;
    private TextView progressTextView;
    private RadioGroup optionsRadioGroup;
    private Button nextButton;
    private RecyclerView resultsRecyclerView;
    private QuestionsResultsAdapter resultsAdapter;

    public static Intent newIntent(Context context, String seriesId, String episodeId) {
        Intent intent = new Intent(context, QuestionsActivity.class);
        intent.putExtra(EXTRA_SERIES_ID, seriesId);
        intent.putExtra(EXTRA_EPISODE_ID, episodeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        contentManager = new ContentManager(this);

        String seriesId = getIntent().getStringExtra(EXTRA_SERIES_ID);
        String episodeId = getIntent().getStringExtra(EXTRA_EPISODE_ID);
        
        if (seriesId == null || episodeId == null) {
            Log.e(TAG, "Missing series_id or episode_id");
            Toast.makeText(this, "Error: Missing series or episode information", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentSeries = contentManager.getSeriesById(seriesId);
        if (currentSeries.isEmpty()) {
            Log.e(TAG, "Series not found: " + seriesId);
            Toast.makeText(this, "Error: Series not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        loadQuestions(episodeId);
    }

    private void initializeViews() {
        questionTextView = findViewById(R.id.questionTextView);
        progressTextView = findViewById(R.id.progressTextView);
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup);
        nextButton = findViewById(R.id.nextButton);
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView);

        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        resultsAdapter = new QuestionsResultsAdapter();
        resultsRecyclerView.setAdapter(resultsAdapter);
        resultsRecyclerView.setVisibility(View.GONE);

        nextButton.setOnClickListener(v -> checkAnswerAndProceed());
    }

    private void loadQuestions(String episodeId) {
        Log.d(TAG, "Loading questions for episode: " + episodeId);
        
        currentSeries.ifPresent(series -> {
            String filename = series.getId() + "_" + episodeId + ".json";
            Series loadedSeries = contentManager.loadSeriesFile(filename);
            
            if (loadedSeries == null) {
                Log.e(TAG, "Failed to load series file: " + filename);
                Toast.makeText(this, "Error: Failed to load questions", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Преобразуем строковые вопросы в объекты Question
            questions = new ArrayList<>();
            for (String questionJson : loadedSeries.getQuestions()) {
                try {
                    JSONObject json = new JSONObject(questionJson);
                    Question question = new Question(
                        json.getString("id"),
                        json.getString("type"),
                        json.getString("question"),
                        jsonArrayToList(json.getJSONArray("options")),
                        json.get("correctAnswer")
                    );
                    questions.add(question);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing question JSON", e);
                }
            }

            if (questions.isEmpty()) {
                Log.e(TAG, "No questions found in series file: " + filename);
                Toast.makeText(this, "Error: No questions available", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            Log.d(TAG, "Loaded " + questions.size() + " questions");
            updateProgress();
            displayCurrentQuestion();
        });
    }

    private List<String> jsonArrayToList(JSONArray array) throws JSONException {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }
        return list;
    }

    private void updateProgress() {
        progressTextView.setText(String.format("%d/%d", 
            currentQuestionIndex + 1, questions.size()));
    }

    private void displayCurrentQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            showResults();
            return;
        }

        Question currentQuestion = questions.get(currentQuestionIndex);
        questionTextView.setText(currentQuestion.getQuestion());
        
        optionsRadioGroup.removeAllViews();
        for (String option : currentQuestion.getOptions()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            optionsRadioGroup.addView(radioButton);
        }

        updateProgress();
    }

    private void checkAnswerAndProceed() {
        int selectedId = optionsRadioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedButton = findViewById(selectedId);
        String selectedAnswer = selectedButton.getText().toString();
        
        List<String> selectedAnswers = new ArrayList<>();
        selectedAnswers.add(selectedAnswer);
        
        Question currentQuestion = questions.get(currentQuestionIndex);
        boolean isCorrect = currentQuestion.isCorrectAnswer(selectedAnswers);
        
        userAnswers.add(selectedAnswer);
        showAnswerFeedback(isCorrect);
        
        currentQuestionIndex++;
        displayCurrentQuestion();
    }

    private void showAnswerFeedback(boolean isCorrect) {
        String message = isCorrect ? "Correct!" : "Incorrect";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showResults() {
        try {
            JSONArray questionsArray = new JSONArray();
            JSONArray resultsArray = new JSONArray();
            JSONArray userAnswersArray = new JSONArray();

            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                String userAnswer = userAnswers.get(i);
                boolean isCorrect = question.isCorrectAnswer(List.of(userAnswer));

                questionsArray.put(question.toJSONObject());
                resultsArray.put(new JSONObject().put("isCorrect", isCorrect));
                userAnswersArray.put(userAnswer);
            }

            Intent intent = QuestionsResultsActivity.newIntent(
                this,
                questionsArray.toString(),
                resultsArray.toString(),
                userAnswersArray.toString()
            );
            startActivity(intent);
            finish();
        } catch (JSONException e) {
            Log.e(TAG, "Error creating results JSON", e);
            Toast.makeText(this, "Error showing results", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentQuestionIndex", currentQuestionIndex);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex", 0);
    }
} 