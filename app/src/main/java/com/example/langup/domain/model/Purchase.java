package com.example.langup.domain.model;

import com.google.firebase.Timestamp;

public class Purchase {
    private String seriesId;
    private Timestamp purchaseDate;
    private Timestamp expiryDate;
    private String status;

    public Purchase() {
        // Required empty constructor for Firebase
    }

    public Purchase(String seriesId, Timestamp purchaseDate, Timestamp expiryDate, String status) {
        this.seriesId = seriesId;
        this.purchaseDate = purchaseDate;
        this.expiryDate = expiryDate;
        this.status = status;
    }

    // Getters and setters
    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public Timestamp getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Timestamp getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Timestamp expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 