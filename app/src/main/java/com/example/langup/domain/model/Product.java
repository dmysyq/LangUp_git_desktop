package com.example.langup.domain.model;

public class Product {
    private String id;
    private String title;
    private double price;
    private String currency;
    private String description;
    private String type; // "single" or "subscription"

    public Product() {
        // Required empty constructor for Firebase
    }

    public Product(String id, String title, double price, String currency, String description, String type) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.currency = currency;
        this.description = description;
        this.type = type;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
} 