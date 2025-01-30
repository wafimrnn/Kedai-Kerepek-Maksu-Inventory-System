package com.model;

import java.math.BigDecimal;

public class InventoryReport {
    private String productName;
    private int quantityInStock;
    private BigDecimal productPrice;
    private BigDecimal totalValue;

    // Constructor
    public InventoryReport(String productName, int quantityInStock, BigDecimal productPrice) {
        this.productName = productName;
        this.quantityInStock = quantityInStock;
        this.productPrice = productPrice;
        this.totalValue = productPrice.multiply(BigDecimal.valueOf(quantityInStock));
    }

    // Getters
    public String getProductName() {
        return productName;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }
}