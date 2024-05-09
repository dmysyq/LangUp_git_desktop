package com.example.langup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat soundEffectsSwitch;
    private SwitchCompat vibrationSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        soundEffectsSwitch = findViewById(R.id.soundEffectsSwitch);
        vibrationSwitch = findViewById(R.id.vibrationSwitch);

        loadSettings();

        findViewById(R.id.saveButton).setOnClickListener(view -> saveSettings());
    }

    private void loadSettings() {
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        soundEffectsSwitch.setChecked(preferences.getBoolean("SoundEffects", true));
        vibrationSwitch.setChecked(preferences.getBoolean("Vibration", true));
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putBoolean("SoundEffects", soundEffectsSwitch.isChecked());
        editor.putBoolean("Vibration", vibrationSwitch.isChecked());
        editor.apply();
        Toast.makeText(this, "Настройки сохранены", Toast.LENGTH_SHORT).show();
    }
}
