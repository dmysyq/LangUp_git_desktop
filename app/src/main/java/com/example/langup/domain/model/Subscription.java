package com.example.langup.domain.model;

import com.google.firebase.Timestamp;

public class Subscription {
    private String userId;
    private String productId;
    private String type;
    private Timestamp startDate;
    private Timestamp endDate;
    private boolean isActive;

    public Subscription() {
        // Required empty constructor for Firestore
    }

    public Subscription(String userId, String productId, String type, Timestamp startDate, Timestamp endDate, boolean isActive) {
        this.userId = userId;
        this.productId = productId;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getProductId() { return productId; }
    public String getType() { return type; }
    public Timestamp getStartDate() { return startDate; }
    public Timestamp getEndDate() { return endDate; }
    public boolean isActive() { return isActive; }
    public boolean getIsActive() { return isActive; }

    // Setters
    public void setUserId(String userId) { this.userId = userId; }
    public void setProductId(String productId) { this.productId = productId; }
    public void setType(String type) { this.type = type; }
    public void setStartDate(Timestamp startDate) { this.startDate = startDate; }
    public void setEndDate(Timestamp endDate) { this.endDate = endDate; }
    public void setActive(boolean active) { isActive = active; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }
} 