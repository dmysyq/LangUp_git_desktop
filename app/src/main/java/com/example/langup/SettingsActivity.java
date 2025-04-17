package com.example.langup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.SwitchCompat;
import com.example.langup.base.BaseActivity;
import com.example.langup.utils.LocaleManager;

public class SettingsActivity extends BaseActivity {
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
        
        String currentLanguage = localeManager.getCurrentLanguage();
        languageSpinner.setSelection(currentLanguage.equals("ru") ? 1 : 0);
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
        String languageCode = languageSpinner.getSelectedItemPosition() == 1 ? "ru" : "en";
        editor.putString("language", languageCode);
        updateLocale(languageCode);

        editor.apply();
        Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_SHORT).show();
    }
}
