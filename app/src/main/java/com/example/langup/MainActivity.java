package com.example.langup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.langup.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.containerHouseOfTheDragons).setOnClickListener(v -> onMovieSelected("Houseofthedragon.json"));
        findViewById(R.id.containerDune).setOnClickListener(v -> onMovieSelected("Dune.json"));
        findViewById(R.id.containerInterstellar).setOnClickListener(v -> onMovieSelected("Interstellar.json"));
        findViewById(R.id.containerWhatsEatingGilbertGrape).setOnClickListener(v -> onMovieSelected("Gilbertgrape.json"));
        findViewById(R.id.containerStarWarsRevengeoftheSith).setOnClickListener(v -> onMovieSelected("Starwarsep3.json"));

        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        ImageButton profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });
    }

    private void onMovieSelected(String jsonFileName) {
        Intent intent = new Intent(MainActivity.this, SeriesActivity.class);
        intent.putExtra("jsonFileName", jsonFileName);
        startActivity(intent);
    }
}
