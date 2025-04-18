package com.example.langup.utils;

import android.content.Context;
import com.example.langup.models.QuestionsData;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class QuestionsManager {
    private static final String QUESTIONS_DIR = "questions/";
    private final Context context;
    private final Gson gson;

    public QuestionsManager(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    public QuestionsData loadQuestions(String filename) {
        try {
            InputStream is = context.getAssets().open(QUESTIONS_DIR + filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            return gson.fromJson(json, QuestionsData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkAnswer(QuestionsData.Question question, int selectedAnswer) {
        if (question.isSingleChoice()) {
            return question.getCorrectAnswer() == selectedAnswer;
        }
        return false;
    }

    public boolean checkAnswers(QuestionsData.Question question, List<Integer> selectedAnswers) {
        if (question.isMultipleChoice()) {
            List<Integer> correctAnswers = question.getCorrectAnswers();
            if (selectedAnswers.size() != correctAnswers.size()) {
                return false;
            }
            return selectedAnswers.containsAll(correctAnswers);
        }
        return false;
    }
} 