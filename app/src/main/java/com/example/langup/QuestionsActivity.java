package com.example.langup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langup.adapters.QuestionsAdapter;
import com.example.langup.models.Episode;
import com.example.langup.models.Question;
import com.example.langup.utils.ContentManager;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestionsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private QuestionsAdapter adapter;
    private Button checkButton;
    private Button watchVideoButton;
    private TextView titleTextView;
    private TextView progressTextView;
    private TextView questionTypeTextView;
    private TextView questionTextView;
    private RadioGroup radioGroup;
    private LinearLayout checkboxContainer;
    private MaterialButton actionButton;
    private View currentQuestionView;
    private View nextQuestionView;

    private List<JSONObject> questions;
    private int currentQuestionIndex = 0;
    private boolean isCheckingAnswer = false;
    private List<Boolean> results;
    private List<Set<Integer>> userAnswers;

    private String episodeId;
    private String youtubeUrl;
    private Toolbar toolbar;

    private static final String EXTRA_SERIES_ID = "extra_series_id";
    private static final String EXTRA_EPISODE_ID = "extra_episode_id";

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

        String seriesId = getIntent().getStringExtra(EXTRA_SERIES_ID);
        episodeId = getIntent().getStringExtra(EXTRA_EPISODE_ID);

        if (episodeId == null) {
            Toast.makeText(this, R.string.error_loading_episode, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        setupToolbar();
        loadQuestions(seriesId, episodeId);
        results = new ArrayList<>();
        userAnswers = new ArrayList<>();
        showQuestion(currentQuestionIndex);
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.questions_title);

        recyclerView = findViewById(R.id.recyclerView);
        checkButton = findViewById(R.id.checkButton);
        watchVideoButton = findViewById(R.id.watchVideoButton);
        titleTextView = findViewById(R.id.titleTextView);
        progressTextView = findViewById(R.id.progressTextView);
        questionTypeTextView = findViewById(R.id.questionTypeTextView);
        questionTextView = findViewById(R.id.questionTextView);
        radioGroup = findViewById(R.id.radioGroup);
        checkboxContainer = findViewById(R.id.checkboxContainer);
        actionButton = findViewById(R.id.actionButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuestionsAdapter();
        recyclerView.setAdapter(adapter);

        checkButton.setOnClickListener(v -> checkAnswers());
        watchVideoButton.setOnClickListener(v -> openYoutubeVideo());
        actionButton.setOnClickListener(v -> onActionButtonClick());
    }

    private void setupToolbar() {
        titleTextView.setText(R.string.questions);
    }

    private void loadQuestions(String seriesId, String episodeId) {
        try {
            String jsonFileName = seriesId + "_" + episodeId + ".json";
            String jsonString = loadJSONFromAsset(jsonFileName);
            JSONObject json = new JSONObject(jsonString);
            JSONArray questionsArray = json.getJSONArray("questions");
            
            questions = new ArrayList<>();
            for (int i = 0; i < questionsArray.length(); i++) {
                questions.add(questionsArray.getJSONObject(i));
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

    private void showQuestion(int index) {
        try {
            JSONObject question = questions.get(index);
            String type = question.getString("type");
            String questionText = question.getString("question");
            JSONArray options = question.getJSONArray("options");

            progressTextView.setText(String.format("%d/%d", index + 1, questions.size()));
            questionTextView.setText(questionText);

            if ("single_choice".equals(type)) {
                setupSingleChoiceQuestion(options);
            } else {
                setupMultipleChoiceQuestion(options);
            }

            questionTypeTextView.setText("single_choice".equals(type) ? 
                    getString(R.string.select_one_answer) : 
                    getString(R.string.select_multiple_answers));

            actionButton.setText(R.string.check_answer);
            actionButton.setEnabled(false);
            isCheckingAnswer = false;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupSingleChoiceQuestion(JSONArray options) throws JSONException {
        radioGroup.setVisibility(View.VISIBLE);
        checkboxContainer.setVisibility(View.GONE);
        radioGroup.removeAllViews();

        for (int i = 0; i < options.length(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(options.getString(i));
            radioButton.setBackground(getDrawable(R.drawable.custom_radio_button));
            radioButton.setTextColor(getColorStateList(R.color.answer_text_color));
            radioButton.setPadding(32, 24, 32, 24);
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 16);
            radioButton.setLayoutParams(params);
            
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> 
                    actionButton.setEnabled(true));
            
            radioGroup.addView(radioButton);
        }
    }

    private void setupMultipleChoiceQuestion(JSONArray options) throws JSONException {
        radioGroup.setVisibility(View.GONE);
        checkboxContainer.setVisibility(View.VISIBLE);
        checkboxContainer.removeAllViews();

        for (int i = 0; i < options.length(); i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(options.getString(i));
            checkBox.setBackground(getDrawable(R.drawable.custom_checkbox));
            checkBox.setTextColor(getColorStateList(R.color.answer_text_color));
            checkBox.setPadding(32, 24, 32, 24);
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 16);
            checkBox.setLayoutParams(params);
            
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Enable action button if at least one checkbox is checked
                boolean anyChecked = false;
                for (int j = 0; j < checkboxContainer.getChildCount(); j++) {
                    if (((CheckBox) checkboxContainer.getChildAt(j)).isChecked()) {
                        anyChecked = true;
                        break;
                    }
                }
                actionButton.setEnabled(anyChecked);
            });
            
            checkboxContainer.addView(checkBox);
        }
    }

    private void onActionButtonClick() {
        if (!isCheckingAnswer) {
            checkAnswer();
        } else if (currentQuestionIndex < questions.size() - 1) {
            animateToNextQuestion();
        } else {
            showResults();
        }
    }

    private void checkAnswer() {
        try {
            JSONObject question = questions.get(currentQuestionIndex);
            boolean isCorrect = false;
            Set<Integer> selectedAnswers = new HashSet<>();

            if ("single_choice".equals(question.getString("type"))) {
                int correctAnswer = question.getInt("correctAnswer");
                for (int i = 0; i < radioGroup.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                    radioButton.setEnabled(false);
                    if (radioButton.isChecked()) {
                        selectedAnswers.add(i);
                        isCorrect = (i == correctAnswer);
                    }
                    if (i == correctAnswer) {
                        radioButton.setChecked(true);
                    }
                }
            } else {
                JSONArray correctAnswers = question.getJSONArray("correctAnswers");
                Set<Integer> correctSet = new HashSet<>();
                for (int i = 0; i < correctAnswers.length(); i++) {
                    correctSet.add(correctAnswers.getInt(i));
                }

                for (int i = 0; i < checkboxContainer.getChildCount(); i++) {
                    CheckBox checkBox = (CheckBox) checkboxContainer.getChildAt(i);
                    checkBox.setEnabled(false);
                    if (checkBox.isChecked()) {
                        selectedAnswers.add(i);
                    }
                    if (correctSet.contains(i)) {
                        checkBox.setChecked(true);
                    }
                }
                isCorrect = correctSet.equals(selectedAnswers);
            }

            results.add(isCorrect);
            userAnswers.add(selectedAnswers);
            actionButton.setText(currentQuestionIndex < questions.size() - 1 ? 
                    R.string.next : R.string.show_results);
            isCheckingAnswer = true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void animateToNextQuestion() {
        ObjectAnimator slideOut = ObjectAnimator.ofFloat(questionTextView, "translationX", 0f, -questionTextView.getWidth());
        slideOut.setDuration(300);
        slideOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentQuestionIndex++;
                showQuestion(currentQuestionIndex);
                questionTextView.setTranslationX(questionTextView.getWidth());
                ObjectAnimator slideIn = ObjectAnimator.ofFloat(questionTextView, "translationX", questionTextView.getWidth(), 0f);
                slideIn.setDuration(300);
                slideIn.start();
            }
        });
        slideOut.start();
    }

    private void showResults() {
        try {
            // Convert questions to JSON array
            JSONArray questionsArray = new JSONArray();
            for (JSONObject question : questions) {
                questionsArray.put(question);
            }

            // Convert results to JSON array
            JSONArray resultsArray = new JSONArray();
            for (Boolean result : results) {
                resultsArray.put(result);
            }

            // Convert user answers to JSON array
            JSONArray userAnswersArray = new JSONArray();
            for (Set<Integer> answers : userAnswers) {
                JSONArray answerArray = new JSONArray();
                for (Integer answer : answers) {
                    answerArray.put(answer);
                }
                userAnswersArray.put(answerArray);
            }

            // Start results activity
            startActivity(QuestionsResultsActivity.newIntent(
                this,
                questionsArray.toString(),
                resultsArray.toString(),
                userAnswersArray.toString()
            ));
            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkAnswers() {
        int correctAnswers = 0;
        int totalQuestions = adapter.getItemCount();

        for (int i = 0; i < totalQuestions; i++) {
            Question question = adapter.getQuestion(i);
            List<String> selectedAnswers = adapter.getSelectedAnswers(i);
            
            if (question.isCorrect(selectedAnswers)) {
                correctAnswers++;
            }
        }

        showResults(correctAnswers, totalQuestions);
    }

    private void showResults(int correctAnswers, int totalQuestions) {
        String message = String.format(getString(R.string.correct_answers_count), 
                                      correctAnswers, totalQuestions);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void openYoutubeVideo() {
        if (youtubeUrl != null && !youtubeUrl.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl));
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.error_loading_episode, Toast.LENGTH_SHORT).show();
        }
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