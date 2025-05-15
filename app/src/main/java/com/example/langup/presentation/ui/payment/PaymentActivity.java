package com.example.langup.presentation.ui.payment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langup.R;
import com.example.langup.data.repository.ProductRepository;
import com.example.langup.data.repository.PurchaseRepository;
import com.example.langup.domain.model.Product;

import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    private ProductRepository productRepository;
    private PurchaseRepository purchaseRepository;
    private String seriesId;
    private RecyclerView productsRecyclerView;
    private TextView titleTextView;
    private Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        productRepository = new ProductRepository();
        purchaseRepository = new PurchaseRepository();
        seriesId = getIntent().getStringExtra("seriesId");

        initializeViews();
        setupListeners();
        loadProducts();
    }

    private void initializeViews() {
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        titleTextView = findViewById(R.id.titleTextView);
        closeButton = findViewById(R.id.closeButton);

        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        titleTextView.setText("Choose a Plan");
    }

    private void setupListeners() {
        closeButton.setOnClickListener(v -> finish());
    }

    private void loadProducts() {
        // For now, we'll use default products
        List<Product> products = productRepository.getDefaultProducts();
        ProductAdapter adapter = new ProductAdapter(products, this::handlePurchase);
        productsRecyclerView.setAdapter(adapter);
    }

    private void handlePurchase(Product product) {
        // Simulate payment process
        Toast.makeText(this, "Processing payment...", Toast.LENGTH_SHORT).show();

        // Add purchase to Firebase
        purchaseRepository.addPurchase(seriesId)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Purchase successful!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Purchase failed: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                });
    }
} 