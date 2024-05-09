package com.example.langup;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button logInButton;
    private FirebaseAuth mAuth;

    private AuthManager authManager; // Добавление переменной для AuthManager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Инициализация Firebase Auth и AuthManager
        mAuth = FirebaseAuth.getInstance();
        authManager = new AuthManager(this); // Инициализация AuthManager

        // Связывание элементов интерфейса
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this::onClick);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        logInButton = findViewById(R.id.logInButton);

        ImageButton togglePasswordVisibilityButton = findViewById(R.id.togglePasswordVisibilityButton);
        togglePasswordVisibilityButton.setOnClickListener(new View.OnClickListener() {
            boolean isPasswordVisible = false;
            @Override
            public void onClick(View v) {
                isPasswordVisible = !isPasswordVisible;
                passwordEditText.setInputType(isPasswordVisible ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Авторизация пользователя
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // Сохранение состояния авторизации пользователя через AuthManager
                                authManager.setUserLoggedIn(true);

                                // Переход на MainActivity
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Если вход не удался, отображение сообщения пользователю
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void onClick(View v) {
        onBackPressed();
    }
}
