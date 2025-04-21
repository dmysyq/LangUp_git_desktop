package com.example.langup.presentation.ui.script;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.langup.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ScriptActivity extends AppCompatActivity {
    private static final String EXTRA_SERIES_ID = "extra_series_id";
    private static final String EXTRA_EPISODE_ID = "extra_episode_id";
    private static final String EXTRA_TITLE = "extra_title";

    private TextView titleTextView;
    private TextView scriptTextView;

    public static Intent newIntent(Context context, String seriesId, String episodeId, String title) {
        Intent intent = new Intent(context, ScriptActivity.class);
        intent.putExtra(EXTRA_SERIES_ID, seriesId);
        intent.putExtra(EXTRA_EPISODE_ID, episodeId);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script);

        String seriesId = getIntent().getStringExtra(EXTRA_SERIES_ID);
        String episodeId = getIntent().getStringExtra(EXTRA_EPISODE_ID);
        String title = getIntent().getStringExtra(EXTRA_TITLE);

        initializeViews();
        setupToolbar(title);
        loadScript(seriesId, episodeId);
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
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleTextView.setText(title);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadScript(String seriesId, String episodeId) {
        try {
            String jsonFileName = seriesId + "_" + episodeId + ".json";
            String jsonString = loadJSONFromAsset(jsonFileName);
            JSONObject json = new JSONObject(jsonString);
            JSONArray scriptArray = json.getJSONArray("script");
            
            SpannableStringBuilder scriptBuilder = new SpannableStringBuilder();
            
            for (int i = 0; i < scriptArray.length(); i++) {
                JSONObject line = scriptArray.getJSONObject(i);
                String character = line.getString("character");
                String dialogue = line.getString("dialogue");

                // Add character name in bold
                SpannableString characterSpan = new SpannableString(character + ": ");
                characterSpan.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 
                    0, character.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                
                scriptBuilder.append(characterSpan);
                scriptBuilder.append(dialogue);
                scriptBuilder.append("\n\n");
            }
            
            scriptTextView.setText(scriptBuilder);

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            // Handle error - maybe show an error message to user
            scriptTextView.setText(R.string.error_loading_script);
        }
    }

    private String loadJSONFromAsset(String fileName) throws IOException {
        InputStream is = getAssets().open(fileName);
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        is.close();
        return new String(buffer, StandardCharsets.UTF_8);
    }
} 