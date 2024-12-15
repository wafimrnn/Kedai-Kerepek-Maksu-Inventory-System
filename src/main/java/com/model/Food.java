package com.model;

import java.sql.Date;

public class Food extends Product {
    private double weight; // In grams or kg
    private String packagingType; // "Packet" or "Jar"
    
    // Constructor
    public Food(int productId, String productName, int quantityStock, double price, Date expiryDate, 
                int restockLevel, String imagePath, String productStatus, double weight, String packagingType) {
        // Call the parent class constructor to initialize inherited fields
        super(productId, productName, quantityStock, price, expiryDate, restockLevel, imagePath, productStatus);
        
        // Initialize the new fields specific to the Food class
        this.weight = weight;
        this.packagingType = packagingType;
    }

    // Getter and Setter methods
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getPackagingType() {
        return packagingType;
    }

    public void setPackagingType(String packagingType) {
        this.packagingType = packagingType;
    }
}
