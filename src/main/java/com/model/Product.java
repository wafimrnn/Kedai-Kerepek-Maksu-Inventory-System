package com.model;

import java.sql.Date;

public class Product {
	private int productId;
    private String productName;
    private int quantityStock;
    private double price;
    private Date expiryDate;
    private int restockLevel;
    private String productStatus;
    private String imagePath;  // Added imagePath field

    // Constructor for Product class
    public Product(int productId, String productName, int quantityStock, double price, Date expiryDate,
                   int restockLevel, String productStatus, String imagePath) {
        this.productId = productId;
        this.productName = productName;
        this.quantityStock = quantityStock;
        this.price = price;
        this.expiryDate = expiryDate;
        this.restockLevel = restockLevel;
        this.productStatus = productStatus;
        this.imagePath = imagePath;  // Initialize imagePath
    }

    // Getter and Setter methods

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantityStock() {
        return quantityStock;
    }

    public void setQuantityStock(int quantityStock) {
        this.quantityStock = quantityStock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date string) {
        this.expiryDate = string;
    }

    public int getRestockLevel() {
        return restockLevel;
    }

    public void setRestockLevel(int restockLevel) {
        this.restockLevel = restockLevel;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }
}
