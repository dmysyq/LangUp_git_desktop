package com.example.langup.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.langup.utils.LocaleManager;
import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {
    protected LocaleManager localeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localeManager = LocaleManager.getInstance(this);
        localeManager.applyCurrentLocale();
        super.onCreate(savedInstanceState);
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