package com.example.langup.presentation.ui.base;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.langup.R;
import com.example.langup.domain.utils.LocaleManager;
import com.google.android.material.appbar.MaterialToolbar;

public abstract class BaseActivity extends AppCompatActivity {
    protected LocaleManager localeManager;
    protected MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localeManager = LocaleManager.getInstance(this);
        localeManager.applyCurrentLocale();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    protected abstract int getLayoutResourceId();

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        localeManager.applyCurrentLocale();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(localeManager.onAttach(newBase));
    }

    protected void updateLocale(String language) {
        localeManager.setLocale(language);
        recreate();
    }
}