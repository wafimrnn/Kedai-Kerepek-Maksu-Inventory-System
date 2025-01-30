package com.model;

import java.util.List;

public class Sale {
    private int saleId;
    private String saleDate;
    private double totalAmount;
    private String paymentMethod;
    private int userId;
    private List<SaleItem> saleItems; // A list to hold the associated SaleItem objects
    
    public Sale() {
        // You can initialize the fields with default values, or leave them uninitialized
        this.saleId = 0;             // Default ID value
        this.saleDate = "";          // Default empty date
        this.totalAmount = 0.0;      // Default total amount
        this.paymentMethod = "";     // Default payment method
        this.userId = 0;             // Default user ID
        this.saleItems = null;       // Default null list (or you can initialize it as an empty list)
    }
    
 // Constructor with parameters
    public Sale(int saleId, String saleDate, double totalAmount, String paymentMethod) {
        this.saleId = saleId;
        this.saleDate = saleDate;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.saleItems = null; // Optional: Initialize saleItems as empty if not provided
    }

    // Getters and setters for all fields
    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<SaleItem> getSaleItems() {
        return saleItems;
    }

    public void setSaleItems(List<SaleItem> saleItems) {
        this.saleItems = saleItems;
    }
   
}
