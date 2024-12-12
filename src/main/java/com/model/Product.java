package com.model; // Use your actual package here

public class Product {
    private int productId;
    private String productName;
    private String imagePath;
    private double price;
    
    // Constructor
    public Product(int productId,String productName,String imagePath, int quantity,String category, double price) {
        this.productId = productId;
        this.productName = productName;
        this.imagePath = imagePath;
        this.price = price;
    }

    // Getter and Setter methods
    public int getId() {
        return productId;
    }

    public void setId(int id) {
        this.productId = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    // You can also override toString(), equals(), hashCode() if needed
}