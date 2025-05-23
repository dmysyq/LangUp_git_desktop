package com.example.langup.presentation.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.langup.R;
import com.google.android.material.slider.Slider;

public class SearchFilterDialog extends Dialog {
    private static final String PREFS_NAME = "filter_preferences";
    private static final String KEY_DIFFICULTY = "difficulty";
    private static final String KEY_ACCENT = "accent";
    private static final String KEY_SOURCE = "source";

    private final OnSearchFilterListener listener;
    private Slider difficultySlider;
    private TextView difficultyValue;
    private Spinner accentSpinner;
    private Spinner sourceSpinner;
    private Button applyButton;
    private Button cancelButton;
    private Button resetButton;
    private final SharedPreferences preferences;

    public interface OnSearchFilterListener {
        void onSearchFilter(int difficultyLevel, String accent, String source);
        void onResetFilters();
    }

    public SearchFilterDialog(@NonNull Context context, OnSearchFilterListener listener) {
        super(context, R.style.DialogTheme);
        this.listener = listener;
        this.preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_search_filter);

        // Настройка размеров диалога
        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        initializeViews();
        setupSpinners();
        setupSlider();
        setupButtons();
        loadSavedState();
    }

    private void initializeViews() {
        difficultySlider = findViewById(R.id.difficulty_slider);
        difficultyValue = findViewById(R.id.difficulty_value);
        accentSpinner = findViewById(R.id.accent_spinner);
        sourceSpinner = findViewById(R.id.source_spinner);
        applyButton = findViewById(R.id.apply_button);
        cancelButton = findViewById(R.id.cancel_button);
        resetButton = findViewById(R.id.reset_button);
    }

    private void setupSlider() {
        difficultySlider.addOnChangeListener((slider, value, fromUser) -> {
            int intValue = (int) value;
            if (intValue == 0) {
                difficultyValue.setText(R.string.all_levels);
            } else {
                difficultyValue.setText(String.valueOf(intValue));
            }
        });

        // Установка начального значения
        if (difficultySlider.getValue() == 0) {
            difficultyValue.setText(R.string.all_levels);
        } else {
            difficultyValue.setText(String.valueOf((int) difficultySlider.getValue()));
        }
    }

    private void setupSpinners() {
        // Настройка спиннера акцента
        String[] accents = new String[]{
                getContext().getString(R.string.all_accents),
                getContext().getString(R.string.american),
                getContext().getString(R.string.british),
                getContext().getString(R.string.south_african),
                getContext().getString(R.string.new_zealand),
                getContext().getString(R.string.australian),
                getContext().getString(R.string.other_accent)
        };
        ArrayAdapter<String> accentAdapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, accents);
        accentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accentSpinner.setAdapter(accentAdapter);

        // Настройка спиннера источника
        String[] sources = new String[]{
                getContext().getString(R.string.all_sources),
                "Netflix",
                "YouTube",
                "Hulu",
                "Amazon Prime",
                "Disney+"
        };
        ArrayAdapter<String> sourceAdapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, sources);
        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceSpinner.setAdapter(sourceAdapter);
    }

    private void setupButtons() {
        applyButton.setOnClickListener(v -> {
            int difficultyLevel = (int) difficultySlider.getValue();
            String accent = accentSpinner.getSelectedItemPosition() == 0 ? "" : 
                           (String) accentSpinner.getSelectedItem();
            String source = sourceSpinner.getSelectedItemPosition() == 0 ? "" : 
                          (String) sourceSpinner.getSelectedItem();
            
            saveState(difficultyLevel, accent, source);
            
            if (listener != null) {
                listener.onSearchFilter(difficultyLevel, accent, source);
            }
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());

        resetButton.setOnClickListener(v -> {
            resetToDefaults();
            if (listener != null) {
                listener.onResetFilters();
            }
            dismiss();
        });
    }

    private void loadSavedState() {
        int savedDifficulty = preferences.getInt(KEY_DIFFICULTY, 0);
        String savedAccent = preferences.getString(KEY_ACCENT, "");
        String savedSource = preferences.getString(KEY_SOURCE, "");

        difficultySlider.setValue(savedDifficulty);
        if (savedDifficulty == 0) {
            difficultyValue.setText(R.string.all_levels);
        } else {
            difficultyValue.setText(String.valueOf(savedDifficulty));
        }

        // Установка сохраненного акцента
        if (!savedAccent.isEmpty()) {
            for (int i = 0; i < accentSpinner.getCount(); i++) {
                if (accentSpinner.getItemAtPosition(i).equals(savedAccent)) {
                    accentSpinner.setSelection(i);
                    break;
                }
            }
        }

        // Установка сохраненного источника
        if (!savedSource.isEmpty()) {
            for (int i = 0; i < sourceSpinner.getCount(); i++) {
                if (sourceSpinner.getItemAtPosition(i).equals(savedSource)) {
                    sourceSpinner.setSelection(i);
                    break;
                }
            }
        }
    }

    private void saveState(int difficulty, String accent, String source) {
        preferences.edit()
                .putInt(KEY_DIFFICULTY, difficulty)
                .putString(KEY_ACCENT, accent)
                .putString(KEY_SOURCE, source)
                .apply();
    }

    private void resetToDefaults() {
        preferences.edit().clear().apply();
        difficultySlider.setValue(0);
        difficultyValue.setText(R.string.all_levels);
        accentSpinner.setSelection(0);
        sourceSpinner.setSelection(0);
    }
} 