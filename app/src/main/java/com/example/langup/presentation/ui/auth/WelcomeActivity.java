package com.example.langup.presentation.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;

import com.example.langup.R;
import com.example.langup.presentation.base.BaseActivity;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_welcome;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        Button signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(v -> {
            Intent loginIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        });

        Button getStartedButton = findViewById(R.id.getStartedButton);
        getStartedButton.setOnClickListener(v -> {
            Intent signinIntent = new Intent(WelcomeActivity.this, SignUpActivity.class);
            startActivity(signinIntent);
        });
    }

}
