package com.example.langup.models;

public class ScoringSystem {
    private int basePoints;
    private TimeBonus timeBonus;
    private AccuracyBonus accuracyBonus;

    public ScoringSystem() {
        // Required empty constructor for Firestore
    }

    public ScoringSystem(int basePoints, TimeBonus timeBonus, AccuracyBonus accuracyBonus) {
        this.basePoints = basePoints;
        this.timeBonus = timeBonus;
        this.accuracyBonus = accuracyBonus;
    }

    // Getters and Setters
    public int getBasePoints() { return basePoints; }
    public void setBasePoints(int basePoints) { this.basePoints = basePoints; }

    public TimeBonus getTimeBonus() { return timeBonus; }
    public void setTimeBonus(TimeBonus timeBonus) { this.timeBonus = timeBonus; }

    public AccuracyBonus getAccuracyBonus() { return accuracyBonus; }
    public void setAccuracyBonus(AccuracyBonus accuracyBonus) { this.accuracyBonus = accuracyBonus; }

    // Inner classes for bonus types
    public static class TimeBonus {
        private int threshold;
        private int points;

        public TimeBonus() {
            // Required empty constructor for Firestore
        }

        public TimeBonus(int threshold, int points) {
            this.threshold = threshold;
            this.points = points;
        }

        public int getThreshold() { return threshold; }
        public void setThreshold(int threshold) { this.threshold = threshold; }

        public int getPoints() { return points; }
        public void setPoints(int points) { this.points = points; }
    }

    public static class AccuracyBonus {
        private int threshold;
        private int points;

        public AccuracyBonus() {
            // Required empty constructor for Firestore
        }

        public AccuracyBonus(int threshold, int points) {
            this.threshold = threshold;
            this.points = points;
        }

        public int getThreshold() { return threshold; }
        public void setThreshold(int threshold) { this.threshold = threshold; }

        public int getPoints() { return points; }
        public void setPoints(int points) { this.points = points; }
    }
} 