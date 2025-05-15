package com.example.langup.data.repository;

import com.example.langup.domain.model.Purchase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class PurchaseRepository {
    private final FirebaseFirestore db;
    private final String userId;

    public PurchaseRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public Task<Void> addPurchase(String seriesId) {
        Purchase purchase = new Purchase(
            seriesId,
            Timestamp.now(),
            null, // No expiry for single purchases
            "active"
        );

        return db.collection("users")
                .document(userId)
                .collection("purchases")
                .document(seriesId)
                .set(purchase);
    }

    public Task<DocumentSnapshot> getPurchase(String seriesId) {
        return db.collection("users")
                .document(userId)
                .collection("purchases")
                .document(seriesId)
                .get();
    }

    public Task<Boolean> hasAccess(String seriesId) {
        return getPurchase(seriesId)
                .continueWith(task -> {
                    if (!task.isSuccessful() || task.getResult() == null) {
                        return false;
                    }
                    DocumentSnapshot doc = task.getResult();
                    if (!doc.exists()) {
                        return false;
                    }
                    Purchase purchase = doc.toObject(Purchase.class);
                    return purchase != null && "active".equals(purchase.getStatus());
                });
    }
} 