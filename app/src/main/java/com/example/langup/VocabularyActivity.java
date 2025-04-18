package com.example.langup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.langup.adapters.VocabularyAdapter;
import com.example.langup.models.VocabularyWord;
import com.google.android.material.appbar.MaterialToolbar;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class VocabularyActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private VocabularyAdapter adapter;
    private List<VocabularyWord> vocabularyWords;
    private boolean showTranslations = true;
    private TextView levelTitleTextView;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_vocabulary;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        levelTitleTextView = findViewById(R.id.levelTitleTextView);
        String levelTitle = getIntent().getStringExtra("level_title");
        if (levelTitle != null && !levelTitle.isEmpty()) {
            levelTitleTextView.setText(levelTitle);
        }

        ImageButton toggleTranslationButton = findViewById(R.id.toggleTranslationButton);
        toggleTranslationButton.setOnClickListener(v -> {
            showTranslations = !showTranslations;
            if (adapter != null) {
                adapter.setShowTranslations(showTranslations);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vocabularyWords = new ArrayList<>();
        adapter = new VocabularyAdapter(vocabularyWords, showTranslations);
        recyclerView.setAdapter(adapter);

        loadVocabulary();
    }

    private void loadVocabulary() {
        try {
            String jsonFileName = getIntent().getStringExtra("json_file");
            if (jsonFileName == null) {
                return;
            }

            InputStream is = getAssets().open("vocabulary/" + jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONObject obj = new JSONObject(json);
            String title = obj.optString("title", "");
            
            if (getSupportActionBar() != null && levelTitleTextView.getText().toString().isEmpty()) {
                getSupportActionBar().setTitle(title);
            } else {
                levelTitleTextView.setText(title);
            }

            JSONArray wordsArray = obj.getJSONArray("words");
            for (int i = 0; i < wordsArray.length(); i++) {
                JSONObject wordObj = wordsArray.getJSONObject(i);
                VocabularyWord word = new VocabularyWord(
                    wordObj.getString("word"),
                    wordObj.getString("definition"),
                    wordObj.getString("translation")
                );
                vocabularyWords.add(word);
            }
            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 