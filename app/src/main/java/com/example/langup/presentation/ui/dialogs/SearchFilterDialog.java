package com.example.langup.presentation.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.example.langup.R;

public class SearchFilterDialog extends Dialog {
    private final OnSearchFilterListener listener;
    private EditText searchEditText;
    private Spinner difficultySpinner;
    private Spinner accentSpinner;
    private Button applyButton;
    private Button cancelButton;

    public interface OnSearchFilterListener {
        void onSearchFilter(String searchQuery, int difficultyLevel, String accent);
    }

    public SearchFilterDialog(@NonNull Context context, OnSearchFilterListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_search_filter);

        initializeViews();
        setupSpinners();
        setupButtons();
    }

    private void initializeViews() {
        searchEditText = findViewById(R.id.search_edit_text);
        difficultySpinner = findViewById(R.id.difficulty_spinner);
        accentSpinner = findViewById(R.id.accent_spinner);
        applyButton = findViewById(R.id.apply_button);
        cancelButton = findViewById(R.id.cancel_button);
    }

    private void setupSpinners() {
        // Настройка спиннера сложности
        String[] difficultyLevels = new String[]{
                getContext().getString(R.string.all_levels),
                getContext().getString(R.string.level_1),
                getContext().getString(R.string.level_2),
                getContext().getString(R.string.level_3),
                getContext().getString(R.string.level_4),
                getContext().getString(R.string.level_5),
                getContext().getString(R.string.level_6)
        };
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, difficultyLevels);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyAdapter);

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
    }

    private void setupButtons() {
        applyButton.setOnClickListener(v -> {
            String searchQuery = searchEditText.getText().toString();
            int difficultyLevel = difficultySpinner.getSelectedItemPosition();
            String accent = accentSpinner.getSelectedItemPosition() == 0 ? "" : 
                           (String) accentSpinner.getSelectedItem();
            
            if (listener != null) {
                listener.onSearchFilter(searchQuery, difficultyLevel, accent);
            }
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());
    }
} 