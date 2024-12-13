package com.model;

import java.util.Date;

public class Product {

    private int productId;
    private String productName;
    private String imagePath;
    private int quantity;
    private String category;
    private double price;
    private Date expiryDate; // Add expiryDate field

    public Product(int productId, String productName, String imagePath, int quantity, String category, double price, Date expiryDate) {
        this.productId = productId;
        this.productName = productName;
        this.imagePath = imagePath;
        this.quantity = quantity;
        this.category = category;
        this.price = price;
        this.expiryDate = expiryDate;
    }

    public Product(int int1, String string, String string2, int int2, double double1, java.sql.Date date, int int3,
			double double2, String string3, double double3) {
		// TODO Auto-generated constructor stub
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
}
