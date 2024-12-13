package com.model;

import java.util.Date;

public class Product {
	private int productId;
    private String productName;
    private String category;
    private int quantity;
    private double price;
    private Date expiryDate;
    private int restockLevel;
    private double weight;
    private String packagingType;
    private double volume;
    private String imagePath;

    public Product(int productId, String productName, String category, int quantity, double price, java.sql.Date expiryDate, int restockLevel, double weight, String packagingType, double volume, String imagePath) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.expiryDate = expiryDate;
        this.restockLevel = restockLevel;
        this.weight = weight;
        this.packagingType = packagingType;
        this.volume = volume;
        this.imagePath = imagePath;
    }


	// Getters and setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    public int getRestockLevel() {
        return restockLevel;
    }

    public double getWeight() {
        return weight;
    }

    public String getPackagingType() {
        return packagingType;
    }

    public double getVolume() {
        return volume;
    }

}
