package com.model;

import java.sql.Date;

public class Food extends Product {
	private String packagingType;
    private Double weight;

    // Constructor for Food class, calling the parent constructor with super()
    public Food(int productId, String productName, int quantityStock, double price, Date expiryDate,
                int restockLevel, String productStatus, String imagePath, String packagingType, Double weight) {
        super(productId, productName, quantityStock, price, expiryDate, restockLevel, productStatus, imagePath);  // Call the parent constructor
        this.packagingType = packagingType;
        this.weight = weight;
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
