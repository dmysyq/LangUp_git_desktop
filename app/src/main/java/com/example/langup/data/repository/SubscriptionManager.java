package com.example.langup.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.langup.domain.model.Subscription;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.payments.paymentlauncher.PaymentLauncher;
import com.stripe.android.payments.paymentlauncher.PaymentResult;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SubscriptionManager {
    private static final String TAG = "SubscriptionManager";
    private static final String SUBSCRIPTIONS_COLLECTION = "subscriptions";
    private static final String PRODUCTS_COLLECTION = "products";
    
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;
    private final Context context;
    private final Stripe stripe;
    private PaymentLauncher paymentLauncher;

    public interface SubscriptionCallback {
        void onSuccess(Subscription subscription);
        void onError(String error);
    }

    public interface PaymentCallback {
        void onSuccess();
        void onError(String error);
    }

    public SubscriptionManager(Context context) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
        
        // Initialize Stripe
        PaymentConfiguration.init(context, "pk_test_51RPUd7P4tVIMsRBYCzX8QuIRynuOpWHVE9V5CgoDeg4zOJ2huEMdiU3nsuFBTD4lmaihlaSenQ0YQcruSbeOM96700YXo7w0NA");
        this.stripe = new Stripe(context, PaymentConfiguration.getInstance(context).getPublishableKey());
    }

    public void setPaymentLauncher(PaymentLauncher paymentLauncher) {
        this.paymentLauncher = paymentLauncher;
    }

    public void checkSubscription(SubscriptionCallback callback) {
        String userId = auth.getCurrentUser().getUid();
        db.collection("users").document(userId)
            .collection(SUBSCRIPTIONS_COLLECTION)
            .whereEqualTo("isActive", true)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    Subscription subscription = queryDocumentSnapshots.getDocuments().get(0)
                        .toObject(Subscription.class);
                    if (subscription != null) {
                        callback.onSuccess(subscription);
                    } else {
                        callback.onError("Failed to parse subscription data");
                    }
                } else {
                    callback.onError("No active subscription found");
                }
            })
            .addOnFailureListener(e -> callback.onError("Error checking subscription: " + e.getMessage()));
    }

    public void createSubscription(String type, PaymentCallback callback) {
        String userId = auth.getCurrentUser().getUid();
        Map<String, Object> subscriptionData = new HashMap<>();
        subscriptionData.put("userId", userId);
        subscriptionData.put("productId", type.equals("yearly") ? "premium_yearly" : "premium_monthly");
        subscriptionData.put("type", type);
        subscriptionData.put("startDate", new Timestamp(new Date()));
        Calendar calendar = Calendar.getInstance();
        if (type.equals("yearly")) {
            calendar.add(Calendar.DAY_OF_YEAR, 365);
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, 30);
        }
        subscriptionData.put("endDate", new Timestamp(calendar.getTime()));
        subscriptionData.put("isActive", true);
        db.collection("users").document(userId)
            .collection(SUBSCRIPTIONS_COLLECTION)
            .add(subscriptionData)
            .addOnSuccessListener(documentReference -> {
                Log.d(TAG, "Subscription created with ID: " + documentReference.getId());
                callback.onSuccess();
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error creating subscription", e);
                callback.onError("Failed to create subscription: " + e.getMessage());
            });
    }

    public void cancelSubscription(String subscriptionId, PaymentCallback callback) {
        db.collection(SUBSCRIPTIONS_COLLECTION)
            .document(subscriptionId)
            .update("isActive", false)
            .addOnSuccessListener(aVoid -> callback.onSuccess())
            .addOnFailureListener(e -> callback.onError("Failed to cancel subscription: " + e.getMessage()));
    }

    public void processPayment(String paymentIntentClientSecret, PaymentCallback callback) {
        if (paymentLauncher == null) {
            callback.onError("Payment launcher not initialized");
            return;
        }

        ConfirmPaymentIntentParams params = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
            null,
            paymentIntentClientSecret
        );

        paymentLauncher.confirm(params);
    }
} 