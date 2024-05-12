package com.example.langup;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TranslationActivity extends AppCompatActivity {

    private TextView originalText;
    private FlexboxLayout wordsContainer;
    private EditText translationField;
    private TextView livesCounter;
    private Button checkButton;
    private Button continueButton;

    private int lives = 3;
    private int currentLevelIndex = 0;
    private static final int MAX_LEVELS = 10;
    private List<Content> contents;

    private SoundPool soundPool;
    private int successSoundId;
    private int failureSoundId;
    private Vibrator vibrator;
    private boolean isTranslationCorrect = false; // Флаг для отслеживания состояния проверки


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);

        originalText = findViewById(R.id.originalText);
        wordsContainer = findViewById(R.id.wordsContainer);
        translationField = findViewById(R.id.translationField);
        livesCounter = findViewById(R.id.livesCounter);
        checkButton = findViewById(R.id.checkButton);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(audioAttributes)
                .build();
        successSoundId = soundPool.load(this, R.raw.correct_answer, 1);
        failureSoundId = soundPool.load(this, R.raw.bad_answer, 1);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        String contentJson = getIntent().getStringExtra("contentJson");
        parseJsonContent(contentJson);

        updateLivesCounter();

        loadLevel(currentLevelIndex);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    private void parseJsonContent(String contentJson) {
        Gson gson = new Gson();
        Type contentType = new TypeToken<ArrayList<Content>>(){}.getType();
        contents = gson.fromJson(contentJson, contentType);
        Collections.shuffle(contents);
    }

    private void loadLevel(int levelIndex) {
        if (levelIndex < MAX_LEVELS && levelIndex < contents.size()) {
            Content content = contents.get(levelIndex);
            originalText.setText(content.getOriginal());
            createTranslationWordBlocks(content.getTranslation());
            updateProgressCounter();
        } else {
            startActivity(new Intent(TranslationActivity.this, MainActivity.class));
            finish();
        }
    }

    private void updateProgressCounter() {
        TextView progressCounter = findViewById(R.id.progressCounter);
        progressCounter.setText(String.format("%d/%d", currentLevelIndex + 1, MAX_LEVELS));
    }

    public void onCheckButtonClicked(View view) {
        String userTranslation = translationField.getText().toString().trim();
        if (!userTranslation.isEmpty()) {
            String correctTranslation = String.join(" ", contents.get(currentLevelIndex).getTranslation());
            if (userTranslation.equalsIgnoreCase(correctTranslation)) {
                playSuccessSound();
                vibrate();
                currentLevelIndex++;
                if (currentLevelIndex >= MAX_LEVELS || currentLevelIndex >= contents.size()) {
                    // Если все уровни пройдены, возвращаемся на MainActivity
                    Intent intent = new Intent(TranslationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    translationField.setText("");
                    loadLevel(currentLevelIndex); // Загрузка следующего уровня
                }
            } else {
                playFailureSound();
                vibrate();
                lives--;
                updateLivesCounter();
                if (lives <= 0) {
                    // Возвращаемся на MainActivity после потери всех жизней
                    Intent intent = new Intent(TranslationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Очищаем поле ввода и показываем сообщение об ошибке
                    translationField.setText("");
                    Toast.makeText(this, "Неправильно! Осталось жизней: " + lives, Toast.LENGTH_SHORT).show();
                    currentLevelIndex++; // Переход к следующему уровню даже при ошибке
                    if (currentLevelIndex < contents.size()) {
                        loadLevel(currentLevelIndex);
                    } else {
                        // Возвращаемся на MainActivity, если уровни закончились
                        Intent intent = new Intent(TranslationActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }
    }


    private void playSuccessSound() {
        if (soundPool != null) {
            soundPool.play(successSoundId, 1, 1, 1, 0, 1);
        }
    }

    private void updateLivesCounter() {
        livesCounter.setText(String.valueOf(lives));
    }

    private void createTranslationWordBlocks(String[] translationWords) {
        wordsContainer.removeAllViews();
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 8, 8, 8);

        ArrayList<String> shuffledWords = new ArrayList<>(Arrays.asList(translationWords));
        Collections.shuffle(shuffledWords);

        for (String word : shuffledWords) {
            Button wordButton = new Button(this);
            wordButton.setText(word);
            wordButton.setLayoutParams(layoutParams);
            wordButton.setBackground(ContextCompat.getDrawable(this, R.drawable.block_container));
            wordButton.setOnClickListener(view -> translationField.append(word + " "));
            wordsContainer.addView(wordButton);
        }
    }

    public void onDeleteLastWordClicked(View view) {
        String currentText = translationField.getText().toString().trim();
        if (!currentText.isEmpty()) {
            int lastSpaceIndex = currentText.lastIndexOf(" ");
            if (lastSpaceIndex != -1) {
                String newText = currentText.substring(0, lastSpaceIndex) + " ";
                translationField.setText(newText);
            } else {
                translationField.setText(" ");
            }
        }
    }

    private void vibrate() {
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(100);
            }
        }
    }

    private void playFailureSound() {
        if (soundPool != null) {
            soundPool.play(failureSoundId, 1, 1, 1, 0, 1);
        }
    }

    private void playSound(int soundId) {
        if (soundPool != null) {
            soundPool.play(soundId, 1, 1, 1, 0, 1);
        }
    }

    private void showCorrectAnswer(String correctTranslation) {
        translationField.setText(correctTranslation);
    }

    private void showCheckButton() {
        checkButton.setVisibility(View.VISIBLE);
        continueButton.setVisibility(View.INVISIBLE);
    }

    private void showContinueButton() {
        checkButton.setVisibility(View.INVISIBLE);
        continueButton.setVisibility(View.VISIBLE);
    }

    private void onClick(View v) {
        onBackPressed();
    }

    static class Content {
        private String original;
        private String[] translation;

        public String getOriginal() {
            return original;
        }

        public String[] getTranslation() {
            return translation;
        }
    }
}

