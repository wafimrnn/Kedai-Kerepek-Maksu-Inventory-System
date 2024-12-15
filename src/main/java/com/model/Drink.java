package com.model;

import java.sql.Date;

public class Drink extends Product {
    private double volume; // In ml or liters
    
    // Constructor
    public Drink(int productId, String productName, int quantityStock, double price, Date expiryDate, 
                 int restockLevel, String imagePath, String productStatus, double volume) {
        // Call the parent class constructor to initialize inherited fields
        super(productId, productName, quantityStock, price, expiryDate, restockLevel, imagePath, productStatus);
        
        // Initialize the new field specific to the Drink class
        this.volume = volume;
    }

    // Getter and Setter methods

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
