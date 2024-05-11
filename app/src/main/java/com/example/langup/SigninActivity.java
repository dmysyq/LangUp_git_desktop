package com.example.langup;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SigninActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Инициализация Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Связывание элементов интерфейса
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onClick(v));
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signInButton);


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

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Регистрация пользователя
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SigninActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // Регистрация успешна, переход на WelcomeActivity
                                Intent intent = new Intent(SigninActivity.this, WelcomeActivity.class);
                                startActivity(intent);
                                finish(); // Закрыть текущую активность
                            } else {
                                // Если регистрация не удалась, отображение сообщения
                                Toast.makeText(SigninActivity.this, "Authentication failed.",
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
