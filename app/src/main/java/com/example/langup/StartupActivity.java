package com.example.langup;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AuthManager authManager = new AuthManager(this);
        if (authManager.isUserLoggedIn()) {
            // Пользователь авторизован, переход на MainActivity
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // Пользователь не авторизован, переход на WelcomeActivity
            startActivity(new Intent(this, WelcomeActivity.class));
        }
        finish(); // Закрыть StartupActivity после перехода
    }
}
