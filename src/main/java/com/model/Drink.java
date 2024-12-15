package com.model;

import java.sql.Date;

public class Drink extends Product {
	private Integer volume;

    // Constructor for Drink class, calling the parent constructor with super()
    public Drink(int productId, String productName, int quantityStock, double price, Date expiryDate,
                 int restockLevel, String productStatus, String imagePath, Integer volume) {
        super(productId, productName, quantityStock, price, expiryDate, restockLevel, productStatus, imagePath);  // Call the parent constructor
        this.volume = volume;
    }

    // Getter and Setter methods

    public double getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }
}
