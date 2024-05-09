package com.example.langup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Инициализация элементов интерфейса
        nameEditText = findViewById(R.id.nameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        ImageButton backButton = findViewById(R.id.backButton);
        Button saveButton = findViewById(R.id.saveButton);
        Button logoutButton = findViewById(R.id.logoutButton);

        // Инициализация Firebase Auth и Database Reference
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");

        // Инициализация AuthManager
        authManager = new AuthManager(this);

        // Загрузка данных пользователя
        if (currentUser != null) {
            mDatabaseRef.child(currentUser.getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    nameEditText.setText(task.getResult().child("name").getValue(String.class));
                    usernameEditText.setText(task.getResult().child("username").getValue(String.class));
                    emailEditText.setText(currentUser.getEmail());
                    // Пароль не отображается и не может быть изменен
                    passwordEditText.setText("********"); // Показать маскированный пароль
                    passwordEditText.setEnabled(false); // Сделать поле пароля неизменяемым
                } else {
                    Toast.makeText(UserProfileActivity.this, "Не удалось загрузить данные пользователя.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Обработка нажатия на кнопку 'назад'
        backButton.setOnClickListener(v -> onBackPressed());

        // Обработка нажатия на кнопку 'СОХРАНИТЬ'
        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();

            // Проверка на пустые поля email
            if (email.isEmpty()) {
                Toast.makeText(UserProfileActivity.this, "Электронная почта не может быть пустой.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Обновление данных пользователя
            if (currentUser != null) {
                mDatabaseRef.child(currentUser.getUid()).child("name").setValue(name.isEmpty() ? null : name);
                mDatabaseRef.child(currentUser.getUid()).child("username").setValue(username.isEmpty() ? null : username);
                currentUser.updateEmail(email);
                Toast.makeText(UserProfileActivity.this, "Информация сохранена.", Toast.LENGTH_SHORT).show();
            }
        });

        // Обработка нажатия на кнопку 'Выйти'
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            authManager.setUserLoggedIn(false);
            startActivity(new Intent(UserProfileActivity.this, WelcomeActivity.class));
            finish();
        });
    }
}
