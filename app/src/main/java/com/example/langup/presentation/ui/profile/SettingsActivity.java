package com.example.langup.presentation.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.SwitchCompat;

import com.example.langup.R;
import com.example.langup.presentation.base.BaseActivity;
import com.example.langup.presentation.adapter.LanguageSpinnerAdapter;
import com.example.langup.domain.utils.LocaleManager;

public class SettingsActivity extends BaseActivity {
    private SharedPreferences preferences;
    private SwitchCompat soundEffectsSwitch;
    private SwitchCompat vibrationSwitch;
    private Spinner languageSpinner;
    private ImageButton backButton;
    private Button saveButton;
    private LocaleManager localeManager;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("LangUpSettings", MODE_PRIVATE);
        localeManager = new LocaleManager();
        initializeViews();
        setupSpinner();
        loadSettings();
        setupListeners();
    }

    private void initializeViews() {
        soundEffectsSwitch = findViewById(R.id.soundEffectsSwitch);
        vibrationSwitch = findViewById(R.id.vibrationSwitch);
        languageSpinner = findViewById(R.id.languageSpinner);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);
    }

    private void setupSpinner() {
        String[] languages = getResources().getStringArray(R.array.languages);
        LanguageSpinnerAdapter adapter = new LanguageSpinnerAdapter(this, java.util.Arrays.asList(languages));
        languageSpinner.setAdapter(adapter);
    }

    private void loadSettings() {
        soundEffectsSwitch.setChecked(preferences.getBoolean("sound_effects", true));
        vibrationSwitch.setChecked(preferences.getBoolean("vibration", true));
        
        String currentLanguage = preferences.getString("language", "en");
        int position = 0; // Default to English
        
        switch (currentLanguage) {
            case "ru":
                position = 1;
                break;
            case "fr":
                position = 2;
                break;
            case "es":
                position = 3;
                break;
            case "kk":
                position = 4;
                break;
        }
        
        languageSpinner.setSelection(position);
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> onBackPressed());
        saveButton.setOnClickListener(v -> saveSettings());
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = preferences.edit();

        // Save sound effects setting
        editor.putBoolean("sound_effects", soundEffectsSwitch.isChecked());

        // Save vibration setting
        editor.putBoolean("vibration", vibrationSwitch.isChecked());

        // Save language setting
        String languageCode;
        switch (languageSpinner.getSelectedItemPosition()) {
            case 1:
                languageCode = "ru";
                break;
            case 2:
                languageCode = "fr"; // французский
                break;
            case 3:
                languageCode = "es"; // испанский
                break;
            case 4:
                languageCode = "kk"; // казахский
                break;
            default:
                languageCode = "en";
                break;
        }
        editor.putString("language", languageCode);
        super.updateLocale(languageCode);

        editor.apply();
        Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_SHORT).show();
    }
}
