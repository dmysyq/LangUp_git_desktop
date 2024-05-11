package com.example.langup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TranslationActivity extends AppCompatActivity {
    private TextView originalText;
    private FlexboxLayout wordsContainer;
    private ArrayList<String> translationWords;
    private String originalSentence;
    private EditText translationField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);

        originalText = findViewById(R.id.originalText);
        wordsContainer = findViewById(R.id.wordsContainer);
        translationField = findViewById(R.id.translationField);

        // Получаем данные из Intent
        String contentJson = getIntent().getStringExtra("contentJson");
        parseJsonContent(contentJson);

        // Устанавливаем оригинальный текст для перевода
        originalText.setText(originalSentence);

        // Создаем блоки со словами для перевода
        createTranslationWordBlocks();
    }

    private void parseJsonContent(String contentJson) {
        Gson gson = new Gson();
        Type contentType = new TypeToken<ArrayList<Content>>(){}.getType(); // Изменение на ArrayList<Content>
        ArrayList<Content> contents = gson.fromJson(contentJson, contentType);

        // Проверяем, что список контента не пустой
        if (contents != null && !contents.isEmpty()) {
            // Берем первый элемент из списка контента
            Content content = contents.get(0);

            // Заполняем переменные originalSentence и translationWords
            originalSentence = content.getOriginal();
            translationWords = new ArrayList<>(Arrays.asList(content.getTranslation()));
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void createTranslationWordBlocks() {
        wordsContainer.removeAllViews();
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 8, 8, 8); // Установите отступы для блоков

        // Создаем массив для хранения перемешанных слов
        ArrayList<String> shuffledWords = new ArrayList<>(translationWords);
        Collections.shuffle(shuffledWords); // Перемешиваем слова

        for (String word : shuffledWords) {
            Button wordButton = new Button(this);
            wordButton.setText(word);
            wordButton.setLayoutParams(layoutParams);
            wordButton.setOnClickListener(view -> {
                translationField.append(word + " ");
            });
            wordButton.setBackground(ContextCompat.getDrawable(this, R.drawable.block_container));

            wordsContainer.addView(wordButton);
        }
    }

    private LinearLayout createNewRow() {
        LinearLayout newRow = new LinearLayout(this);
        newRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        newRow.setOrientation(LinearLayout.HORIZONTAL);
        wordsContainer.addView(newRow);
        return newRow;
    }

    public void onDeleteLastWordClicked(View view) {
        // Получаем текст из поля для перевода
        String translationText = translationField.getText().toString().trim();

        // Разделяем текст на слова
        String[] words = translationText.split("\\s+");

        if (words.length > 0) {
            // Удаляем последнее слово
            StringBuilder updatedText = new StringBuilder();
            for (int i = 0; i < words.length - 1; i++) {
                updatedText.append(words[i]);
                if (i < words.length - 2) {
                    updatedText.append(" ");
                }
            }

            // Устанавливаем обновленный текст в поле для перевода
            translationField.setText(updatedText.toString());
        }
    }


    // Метод для обработки нажатия на кнопку "ПРОВЕРИТЬ"
    public void onCheckButtonClicked(View view) {
        // TODO: Implement logic to check translation accuracy
    }

    class Content {
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
