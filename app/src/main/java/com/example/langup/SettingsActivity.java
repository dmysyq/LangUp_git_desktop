package com.example.langup;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SwitchCompat soundEffectsSwitch;
    private SwitchCompat vibrationSwitch;
    private RadioGroup languageRadioGroup;
    private ImageButton backButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences("LangUpSettings", MODE_PRIVATE);
        initializeViews();
        loadSettings();
        setupListeners();
    }

    private void initializeViews() {
        soundEffectsSwitch = findViewById(R.id.soundEffectsSwitch);
        vibrationSwitch = findViewById(R.id.vibrationSwitch);
        languageRadioGroup = findViewById(R.id.languageRadioGroup);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);
    }

    private void loadSettings() {
        // Load sound effects setting
        boolean isSoundEnabled = preferences.getBoolean("sound_effects", true);
        soundEffectsSwitch.setChecked(isSoundEnabled);

        // Load vibration setting
        boolean isVibrationEnabled = preferences.getBoolean("vibration", true);
        vibrationSwitch.setChecked(isVibrationEnabled);

        // Load language setting
        String currentLanguage = preferences.getString("language", "en");
        switch (currentLanguage) {
            case "en":
                languageRadioGroup.check(R.id.englishRadio);
                break;
            case "ru":
                languageRadioGroup.check(R.id.russianRadio);
                break;
            case "es":
                languageRadioGroup.check(R.id.spanishRadio);
                break;
            case "kk":
                languageRadioGroup.check(R.id.kazakhRadio);
                break;
            case "fr":
                languageRadioGroup.check(R.id.frenchRadio);
                break;
        }
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());

        saveButton.setOnClickListener(v -> {
            saveSettings();
            Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = preferences.edit();

        // Save sound effects setting
        editor.putBoolean("sound_effects", soundEffectsSwitch.isChecked());

        // Save vibration setting
        editor.putBoolean("vibration", vibrationSwitch.isChecked());

        // Save language setting
        int selectedLanguageId = languageRadioGroup.getCheckedRadioButtonId();
        String languageCode = "en"; // Default to English
        if (selectedLanguageId == R.id.russianRadio) {
            languageCode = "ru";
        } else if (selectedLanguageId == R.id.spanishRadio) {
            languageCode = "es";
        } else if (selectedLanguageId == R.id.kazakhRadio) {
            languageCode = "kk";
        } else if (selectedLanguageId == R.id.frenchRadio) {
            languageCode = "fr";
        }
        editor.putString("language", languageCode);
        updateLanguage(languageCode);

        editor.apply();
    }

    private void updateLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
