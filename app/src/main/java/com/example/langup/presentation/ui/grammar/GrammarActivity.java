package com.example.langup.presentation.ui.grammar;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.langup.R;

public class GrammarActivity extends AppCompatActivity {
    private static final String TAG = "GrammarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar);

        String seriesId = getIntent().getStringExtra("series_id");
        String videoUrl = getIntent().getStringExtra("video_url");
        
        Log.d(TAG, "Grammar Activity started with series ID: " + seriesId);
    }
} 