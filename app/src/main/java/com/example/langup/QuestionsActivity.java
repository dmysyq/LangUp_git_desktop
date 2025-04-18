package com.example.langup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.langup.adapters.QuestionsAdapter;
import com.example.langup.models.ContentData;
import com.example.langup.models.ContentData.Episode;
import com.example.langup.models.QuestionsData;
import com.example.langup.utils.ContentManager;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {
    private static final String EXTRA_EPISODE_ID = "episode_id";
    
    private RecyclerView recyclerView;
    private QuestionsAdapter adapter;
    private ContentManager contentManager;
    private MaterialButton checkAnswersButton;
    private MaterialButton watchVideoButton;
    private String youtubeUrl;
    private List<QuestionsData.Question> questions;

    public static Intent newIntent(Context context, String episodeId) {
        Intent intent = new Intent(context, QuestionsActivity.class);
        intent.putExtra(EXTRA_EPISODE_ID, episodeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        String episodeId = getIntent().getStringExtra(EXTRA_EPISODE_ID);
        if (episodeId == null) {
            Toast.makeText(this, R.string.error_loading_episode, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView = findViewById(R.id.questionsRecyclerView);
        checkAnswersButton = findViewById(R.id.checkAnswersButton);
        watchVideoButton = findViewById(R.id.watchVideoButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        contentManager = new ContentManager(this);
        contentManager.getEpisodeById(episodeId).ifPresent(this::setupEpisode);

        checkAnswersButton.setOnClickListener(v -> checkAnswers());
        watchVideoButton.setOnClickListener(v -> openYoutubeVideo());
    }

    private void setupEpisode(Episode episode) {
        setTitle(episode.getTitle());
        youtubeUrl = episode.getYoutubeUrl();
        questions = convertQuestions(episode.getQuestions());
        adapter = new QuestionsAdapter(questions);
        recyclerView.setAdapter(adapter);
    }

    private List<QuestionsData.Question> convertQuestions(List<ContentData.Question> contentQuestions) {
        List<QuestionsData.Question> convertedQuestions = new ArrayList<>();
        for (ContentData.Question contentQuestion : contentQuestions) {
            convertedQuestions.add(new QuestionsData.Question(contentQuestion));
        }
        return convertedQuestions;
    }

    private void checkAnswers() {
        if (adapter != null) {
            int correctAnswers = 0;
            int totalQuestions = adapter.getItemCount();
            
            for (int i = 0; i < totalQuestions; i++) {
                QuestionsData.Question question = adapter.getQuestions().get(i);
                Integer selectedAnswer = adapter.getSelectedAnswer(i);
                
                if (selectedAnswer != null) {
                    if (question.isSingleChoice() && 
                        selectedAnswer == question.getCorrectAnswer()) {
                        correctAnswers++;
                    } else if (question.isMultipleChoice() && 
                             question.getCorrectAnswers().equals(adapter.getSelectedAnswers(i))) {
                        correctAnswers++;
                    }
                }
            }
            
            adapter.showResults();
            String result = getString(R.string.correct_answers_count, correctAnswers, totalQuestions);
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        }
    }

    private void openYoutubeVideo() {
        if (youtubeUrl != null && !youtubeUrl.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl));
            intent.setPackage("com.google.android.youtube");
            
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                // Если приложение YouTube не установлено, открываем в браузере
                intent.setPackage(null);
                startActivity(intent);
            }
        }
    }
} 