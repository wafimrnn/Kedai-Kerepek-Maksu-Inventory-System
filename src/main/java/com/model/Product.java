package com.model;

public class Product {
    private int productId;
    private String productName;
    private String imagePath;
    private double price;
    private int quantity;  // Added quantity
    private String category;  // Added category

    // Constructor with all attributes
    public Product(int productId, String productName, String imagePath, int quantity, String category, double price) {
        this.productId = productId;
        this.productName = productName;
        this.imagePath = imagePath;
        this.price = price;
        this.quantity = quantity;  // Initialize quantity
        this.category = category;  // Initialize category
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Optional: Override toString(), equals(), hashCode() if needed
    @Override
    public String toString() {
        return "Product{id=" + productId + ", name='" + productName + "', price=" + price + ", quantity=" + quantity + ", category='" + category + "'}";
    }
}