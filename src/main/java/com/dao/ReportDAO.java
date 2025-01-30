package com.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.manager.DBConnection;
import com.model.InventoryReport;
import com.model.SalesReport;

public class ReportDAO {

    public List<SalesReport> getSalesReport(java.sql.Date startDate, java.sql.Date endDate) throws SQLException {
        // SQL query to fetch sales data within the provided date range
        String query = "SELECT S.SALE_ID, S.SALE_DATE, U.USER_NAME, P.PROD_NAME, SI.QUANTITY, SI.SUB_TOTAL, S.TOTAL_AMOUNT, S.PAYMENT_METHOD " +
                       "FROM SALES S " +
                       "JOIN SALEITEMS SI ON S.SALE_ID = SI.SALE_ID " +
                       "JOIN PRODUCTS P ON SI.PROD_ID = P.PROD_ID " +
                       "JOIN USERS U ON S.USER_ID = U.USER_ID " +
                       "WHERE S.SALE_DATE BETWEEN ? AND ? " +
                       "ORDER BY S.SALE_DATE";

        List<SalesReport> salesReports = new ArrayList<>();
        
        // Establishing the connection and preparing the statement
        try (Connection conn = DBConnection.getConnection(); // Get connection from DBConnection
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Set the parameters for the prepared statement
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);

            // Execute the query and handle the result set
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Create a new SalesReport object from the result set
                    SalesReport report = new SalesReport(
                        rs.getInt("SALE_ID"),
                        rs.getDate("SALE_DATE"),
                        rs.getString("USER_NAME"),
                        rs.getString("PROD_NAME"),
                        rs.getInt("QUANTITY"),
                        rs.getBigDecimal("SUB_TOTAL"),
                        rs.getBigDecimal("TOTAL_AMOUNT"),
                        rs.getString("PAYMENT_METHOD")
                    );
                    // Add the report to the list
                    salesReports.add(report);
                }
            }
        } catch (SQLException e) {
            // Log the exception or handle it as needed
            e.printStackTrace();
            throw new SQLException("Error fetching sales report data.", e);
        }

        // Return the list of sales reports
        return salesReports;
    }
    
    public List<InventoryReport> getInventoryReport(java.sql.Date startDate, java.sql.Date endDate) throws SQLException {
        String query = "SELECT P.PROD_NAME, P.QUANTITY_STOCK, P.PROD_PRICE " +
                       "FROM PRODUCTS P " +
                       "WHERE P.EXPIRY_DATE BETWEEN ? AND ?";

        List<InventoryReport> inventoryReports = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    InventoryReport report = new InventoryReport(
                        rs.getString("PROD_NAME"),
                        rs.getInt("QUANTITY_STOCK"),
                        rs.getBigDecimal("PROD_PRICE")
                    );
                    inventoryReports.add(report);
                }
            }
        }
        return inventoryReports;
    }
}