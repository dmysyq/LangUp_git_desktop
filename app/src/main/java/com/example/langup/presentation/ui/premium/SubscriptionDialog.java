package com.example.langup.presentation.ui.premium;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.langup.R;

public class SubscriptionDialog extends DialogFragment {
    private static final String MONTHLY_SUBSCRIPTION = "premium_monthly";
    private static final String YEARLY_SUBSCRIPTION = "premium_yearly";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_LangUp);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_subscription, container, false);
        
        TextView benefitsText = view.findViewById(R.id.subscriptionBenefitsText);
        Button monthlyButton = view.findViewById(R.id.monthlySubscriptionButton);
        Button yearlyButton = view.findViewById(R.id.yearlySubscriptionButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        benefitsText.setText(R.string.subscription_benefits);
        monthlyButton.setText(R.string.monthly_subscription);
        yearlyButton.setText(R.string.yearly_subscription);
        cancelButton.setText(R.string.cancel);

        monthlyButton.setOnClickListener(v -> processSubscription(MONTHLY_SUBSCRIPTION));
        yearlyButton.setOnClickListener(v -> processSubscription(YEARLY_SUBSCRIPTION));
        cancelButton.setOnClickListener(v -> dismiss());

        return view;
    }

    private void processSubscription(String subscriptionType) {
        if (getActivity() instanceof PremiumContentActivity) {
            ((PremiumContentActivity) getActivity()).processPayment(subscriptionType);
        }
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
} 