package com.model;

public class SaleItem {
    private int saleId;
    private int prodId;
    private String prodName;  // Add this field
    private int quantity;
    private double subTotal;

    // Default Constructor
    public SaleItem() {}

    // Constructor with prodName
    public SaleItem(int saleId, int prodId, String prodName, int quantity, double subTotal) {
        this.saleId = saleId;
        this.prodId = prodId;
        this.prodName = prodName;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }

    // Getters and setters for all fields
    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {  // Add this method
        return prodName;
    }

    public void setProdName(String prodName) {  // Add this method
        this.prodName = prodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}