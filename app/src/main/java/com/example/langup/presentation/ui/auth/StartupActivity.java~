package com.example.langup.presentation.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import com.example.langup.R;
import com.example.langup.presentation.ui.base.BaseActivity;
import com.example.langup.data.repository.AuthManager;
import com.example.langup.presentation.ui.main.MainActivity;

public class StartupActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AuthManager authManager = new AuthManager(this);
        if (authManager.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, WelcomeActivity.class));
        }
        finish();
    }
    
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_startup;
    }
}
