package com.example.langup.presentation.ui.premium;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langup.R;
import com.example.langup.data.repository.SubscriptionManager;
import com.example.langup.domain.model.Series;
import com.example.langup.domain.model.Subscription;
import com.example.langup.presentation.adapter.SeriesAdapter;
import com.example.langup.presentation.base.BaseActivity;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentMethodCreateParams;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PremiumContentActivity extends BaseActivity implements SeriesAdapter.OnSeriesClickListener {
    private static final String TAG = "PremiumContentActivity";
    
    private RecyclerView recyclerView;
    private SeriesAdapter adapter;
    private Button subscribeButton;
    private TextView subscriptionStatusText;
    private SubscriptionManager subscriptionManager;
    private List<Series> premiumSeries = new ArrayList<>();
    private Stripe stripe;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_premium_content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initializeViews();
        setupToolbar();
        setupSubscriptionManager();
        loadPremiumContent();
        checkSubscriptionStatus();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.premiumContentRecyclerView);
        subscribeButton = findViewById(R.id.subscribeButton);
        subscriptionStatusText = findViewById(R.id.subscriptionStatusText);
        
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new SeriesAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
        
        subscribeButton.setOnClickListener(v -> showSubscriptionDialog());
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.premium_content);
        }
    }

    private void setupSubscriptionManager() {
        subscriptionManager = new SubscriptionManager(this);
        stripe = new Stripe(this, "pk_test_51RPUd7P4tVIMsRBYCzX8QuIRynuOpWHVE9V5CgoDeg4zOJ2huEMdiU3nsuFBTD4lmaihlaSenQ0YQcruSbeOM96700YXo7w0NA");
    }

    private void loadPremiumContent() {
        ArrayList<Series> allSeries = getIntent().getParcelableArrayListExtra("all_series", Series.class);
        if (allSeries != null) {
            premiumSeries = allSeries.stream()
                .filter(series -> series.getMetadata().isPremium())
                .collect(Collectors.toList());
            adapter.updateSeries(premiumSeries);
        }
    }

    private void checkSubscriptionStatus() {
        subscriptionManager.checkSubscription(new SubscriptionManager.SubscriptionCallback() {
            @Override
            public void onSuccess(Subscription subscription) {
                updateUIForSubscribedUser(subscription);
            }

            @Override
            public void onError(String error) {
                updateUIForNonSubscribedUser();
            }
        });
    }

    private void updateUIForSubscribedUser(Subscription subscription) {
        subscribeButton.setVisibility(View.GONE);
        subscriptionStatusText.setVisibility(View.VISIBLE);
        subscriptionStatusText.setText(getString(R.string.subscription_active_until, 
            subscription.getEndDate().toDate().toString()));
    }

    private void updateUIForNonSubscribedUser() {
        subscribeButton.setVisibility(View.VISIBLE);
        subscriptionStatusText.setVisibility(View.GONE);
    }

    private void showSubscriptionDialog() {
        SubscriptionDialog dialog = new SubscriptionDialog();
        dialog.show(getSupportFragmentManager(), "subscription_dialog");
    }

    public void processPayment(String productId) {
        subscriptionManager.createSubscription(productId, new SubscriptionManager.PaymentCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(PremiumContentActivity.this, 
                    R.string.subscription_successful, Toast.LENGTH_SHORT).show();
                checkSubscriptionStatus();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(PremiumContentActivity.this, 
                    error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSeriesClick(Series series) {
        subscriptionManager.checkSubscription(new SubscriptionManager.SubscriptionCallback() {
            @Override
            public void onSuccess(Subscription subscription) {
                startSeriesActivity(series);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(PremiumContentActivity.this, 
                    R.string.premium_content_requires_subscription, 
                    Toast.LENGTH_SHORT).show();
                showSubscriptionDialog();
            }
        });
    }

    private void startSeriesActivity(Series series) {
        // Start the series activity with the selected series
        // Implementation depends on your existing series activity
    }
} 