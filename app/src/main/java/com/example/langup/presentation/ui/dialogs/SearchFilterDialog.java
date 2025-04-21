package com.example.langup.presentation.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.langup.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

public class SearchFilterDialog extends Dialog {
    private OnSearchFilterListener listener;
    private TextInputEditText searchEditText;
    private Slider difficultySlider;
    private TextView difficultyValueText;
    private ChipGroup accentChipGroup;

    public interface OnSearchFilterListener {
        void onSearchFilter(String query, int difficulty, String accent);
    }

    public SearchFilterDialog(Context context, OnSearchFilterListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_search_filter);

        // Настройка окна диалога
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setWindowAnimations(R.style.DialogAnimation);
        }

        searchEditText = findViewById(R.id.searchEditText);
        difficultySlider = findViewById(R.id.difficultySlider);
        difficultyValueText = findViewById(R.id.difficultyValueText);
        accentChipGroup = findViewById(R.id.accentChipGroup);
        MaterialButton cancelButton = findViewById(R.id.cancelButton);
        MaterialButton applyButton = findViewById(R.id.applyButton);

        difficultySlider.addOnChangeListener((slider, value, fromUser) -> {
            difficultyValueText.setText(String.valueOf((int) value));
        });

        cancelButton.setOnClickListener(v -> dismiss());

        applyButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString();
            int difficulty = (int) difficultySlider.getValue();
            String accent = getSelectedAccent();
            
            if (listener != null) {
                listener.onSearchFilter(query, difficulty, accent);
            }
            dismiss();
        });
    }

    private String getSelectedAccent() {
        int selectedChipId = accentChipGroup.getCheckedChipId();
        if (selectedChipId == R.id.accentAmerican) {
            return "American";
        } else if (selectedChipId == R.id.accentBritish) {
            return "British";
        } else if (selectedChipId == R.id.accentAustralian) {
            return "Australian";
        }
        return "";
    }
} 