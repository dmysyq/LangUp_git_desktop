package com.example.langup.presentation.ui.questions;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.langup.R;

public class QuestionsActivity extends AppCompatActivity {
    private static final String TAG = "QuestionsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        String seriesId = getIntent().getStringExtra("series_id");
        String videoUrl = getIntent().getStringExtra("video_url");
        
        Log.d(TAG, "Questions Activity started with series ID: " + seriesId);
    }
} 