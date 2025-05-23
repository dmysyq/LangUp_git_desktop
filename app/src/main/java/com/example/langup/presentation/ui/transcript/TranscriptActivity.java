package com.example.langup.presentation.ui.transcript;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.langup.R;
import com.google.android.material.snackbar.Snackbar;
import com.example.langup.presentation.base.BaseActivity;

public class TranscriptActivity extends BaseActivity {
    private static final String TAG = "TranscriptActivity";
    
    private TextView titleTextView;
    private TextView scriptTextView;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_transcript;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get data from intent
        String title = getIntent().getStringExtra("title");
        String transcript = getIntent().getStringExtra("transcript");

        Log.d(TAG, "onCreate: Received transcript length: " + 
            (transcript != null ? transcript.length() : 0));

        initializeViews();
        setupToolbar(title);
        displayTranscript(transcript);
    }

    private void initializeViews() {
        titleTextView = findViewById(R.id.titleTextView);
        scriptTextView = findViewById(R.id.scriptTextView);
    }

    private void setupToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            titleTextView.setText(title);
        }
    }

    private void displayTranscript(String transcript) {
        if (transcript != null && !transcript.isEmpty()) {
            SpannableString formattedText = formatTranscript(transcript);
            scriptTextView.setText(formattedText);
        } else {
            scriptTextView.setText(R.string.error_loading_script);
            Snackbar.make(scriptTextView, R.string.error_loading_script, 
                Snackbar.LENGTH_LONG).show();
        }
    }

    private SpannableString formatTranscript(String transcript) {
        SpannableString spannableString = new SpannableString(transcript);
        String[] dialogues = transcript.split("\n\n");
        int position = 0;

        for (String dialogue : dialogues) {
            // Highlight character names (before colon)
            if (dialogue.contains(":")) {
                int colonIndex = dialogue.indexOf(":");
                if (colonIndex > 0) {
                    spannableString.setSpan(
                        new ForegroundColorSpan(ContextCompat.getColor(this, R.color.character_name)),
                        position,
                        position + colonIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }
            }

            // Highlight stage directions (text in parentheses)
            int startParen = dialogue.indexOf("(");
            while (startParen != -1) {
                int endParen = dialogue.indexOf(")", startParen);
                if (endParen != -1) {
                    spannableString.setSpan(
                        new ForegroundColorSpan(ContextCompat.getColor(this, R.color.stage_direction)),
                        position + startParen,
                        position + endParen + 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                    startParen = dialogue.indexOf("(", endParen);
                } else {
                    break;
                }
            }

            position += dialogue.length() + 2; // +2 for "\n\n"
        }

        return spannableString;
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