package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.manager.DBConnection;
import com.model.Sale;
import com.model.SaleItem;

public class SaleDAO {

    // Insert a new sale and return the generated sale ID
    public int insertSale(Sale sale) throws SQLException {
        String sql = "INSERT INTO sales (sale_Date, total_Amount, payment_Method, user_Id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            stmt.setDate(1, sale.getSaleDate());
            stmt.setDouble(2, sale.getTotalAmount());
            stmt.setString(3, sale.getPaymentMethod());
            stmt.setInt(4, sale.getUserId());
            stmt.executeUpdate();

            // Retrieve the generated sale ID
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return the auto-incremented SALE_ID
                }
            }
        }
        throw new SQLException("Failed to insert sale.");
    }

    // Insert sale items
    public void insertSaleItems(List<SaleItem> saleItems) throws SQLException {
        String sql = "INSERT INTO salesitems (sale_Id, prod_Id, quantity, sub_total) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (SaleItem item : saleItems) {
                stmt.setInt(1, item.getSaleId());
                stmt.setInt(2, item.getProductId());
                stmt.setInt(3, item.getQuantity());
                stmt.setDouble(4, item.getSubtotal());
                stmt.addBatch();
            }
            stmt.executeBatch(); // Execute all insertions as a batch
        }
    }
}
