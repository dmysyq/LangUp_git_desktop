package com.example.langup.presentation.ui.premium;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.langup.R;
import com.example.langup.data.repository.SubscriptionManager;
import com.stripe.android.payments.paymentlauncher.PaymentLauncher;
import com.stripe.android.payments.paymentlauncher.PaymentResult;
import com.stripe.android.payments.paymentlauncher.PaymentLauncherFactory;
import com.stripe.android.model.ConfirmPaymentIntentParams;

public class SubscribeActivity extends AppCompatActivity {
    private SubscriptionManager subscriptionManager;
    private Button subscribeNowButton;
    private Button monthlyButton;
    private Button yearlyButton;
    private String selectedType = "monthly";
    private PaymentLauncher paymentLauncher;
    private String paymentIntentClientSecret;
    private static final String TEST_PAYMENT_INTENT_SECRET = "pi_3Nw1v2P4tVIMsRBY1_secret_test"; // MOCK, replace with real from backend

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        subscriptionManager = new SubscriptionManager(this);
        subscribeNowButton = findViewById(R.id.subscribeNowButton);
        monthlyButton = findViewById(R.id.monthlyButton);
        yearlyButton = findViewById(R.id.yearlyButton);

        // Default selection: monthly
        setSelectedType("monthly");

        monthlyButton.setOnClickListener(v -> setSelectedType("monthly"));
        yearlyButton.setOnClickListener(v -> setSelectedType("yearly"));
        subscribeNowButton.setOnClickListener(v -> startTestPayment());
    }

    private void setSelectedType(String type) {
        selectedType = type;
        if (type.equals("monthly")) {
            monthlyButton.setBackgroundTintList(getColorStateList(R.color.text_blue));
            monthlyButton.setTextColor(getColor(R.color.white));
            yearlyButton.setBackgroundTintList(getColorStateList(R.color.white));
            yearlyButton.setTextColor(getColor(R.color.text_blue));
        } else {
            yearlyButton.setBackgroundTintList(getColorStateList(R.color.text_blue));
            yearlyButton.setTextColor(getColor(R.color.white));
            monthlyButton.setBackgroundTintList(getColorStateList(R.color.white));
            monthlyButton.setTextColor(getColor(R.color.text_blue));
        }
    }

    private void startTestPayment() {
        subscribeNowButton.setEnabled(false);
        subscribeNowButton.setText(R.string.processing);
        subscriptionManager.createSubscription(selectedType, new SubscriptionManager.PaymentCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(SubscribeActivity.this, R.string.subscription_successful, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
            @Override
            public void onError(String error) {
                Toast.makeText(SubscribeActivity.this, error, Toast.LENGTH_SHORT).show();
                subscribeNowButton.setEnabled(true);
                subscribeNowButton.setText(R.string.subscribe_now);
            }
        });
    }
} 