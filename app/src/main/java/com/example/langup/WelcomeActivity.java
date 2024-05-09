package com.example.langup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        // Настройка кнопки входа
        Button signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(v -> {
            // Переход к LoginActivity
            Intent loginIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        });

        // Настройка кнопки регистрации
        Button getStartedButton = findViewById(R.id.getStartedButton);
        getStartedButton.setOnClickListener(v -> {
            // Переход к SigninActivity
            Intent signinIntent = new Intent(WelcomeActivity.this, SigninActivity.class);
            startActivity(signinIntent);
        });
    }
}
