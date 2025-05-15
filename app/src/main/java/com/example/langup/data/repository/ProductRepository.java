package com.example.langup.data.repository;

import com.example.langup.domain.model.Product;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;
import java.util.ArrayList;

public class ProductRepository {
    private final FirebaseFirestore db;

    public ProductRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public Task<QuerySnapshot> getAllProducts() {
        return db.collection("products")
                .get();
    }

    public Task<QuerySnapshot> getProductsByType(String type) {
        return db.collection("products")
                .whereEqualTo("type", type)
                .get();
    }

    public Task<QuerySnapshot> getProductsBySeriesId(String seriesId) {
        return db.collection("products")
                .whereEqualTo("seriesId", seriesId)
                .get();
    }

    public List<Product> getDefaultProducts() {
        List<Product> products = new ArrayList<>();
        
        // Add some default products
        products.add(new Product(
            "premium_monthly",
            "Premium Monthly",
            9.99,
            "USD",
            "Access to all premium content for one month",
            "subscription"
        ));

        products.add(new Product(
            "premium_yearly",
            "Premium Yearly",
            99.99,
            "USD",
            "Access to all premium content for one year",
            "subscription"
        ));

        return products;
    }
} 