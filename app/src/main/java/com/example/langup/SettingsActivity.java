package com.example.langup;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SwitchCompat soundEffectsSwitch;
    private SwitchCompat vibrationSwitch;
    private Spinner languageSpinner;
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
        languageSpinner = findViewById(R.id.languageSpinner);
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
        int position = currentLanguage.equals("ru") ? 1 : 0; // 0 for English, 1 for Russian
        languageSpinner.setSelection(position);
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
        String languageCode = languageSpinner.getSelectedItemPosition() == 1 ? "ru" : "en";
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
