package com.dao;

import java.sql.*;
import java.util.List;
import com.model.Sale;
import com.model.SaleItem;
import com.manager.DBConnection;
import java.sql.SQLException;

public class SaleDAO {
    private Connection connection;

    public SaleDAO() throws SQLException {
        connection = DBConnection.getConnection();
    }

    public int createSale(Sale sale) throws SQLException {
        String insertSaleQuery = "INSERT INTO SALES (SALE_DATE, TOTAL_AMOUNT, PAYMENT_METHOD, USER_ID) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(insertSaleQuery, Statement.RETURN_GENERATED_KEYS);
        pstmt.setTimestamp(1, new Timestamp(sale.getSaleDate().getTime()));
        pstmt.setDouble(2, sale.getTotalAmount());
        pstmt.setString(3, sale.getPaymentMethod());
        pstmt.setInt(4, sale.getUserId());
        pstmt.executeUpdate();

        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        } else {
            throw new SQLException("Failed to create sale, no ID obtained.");
        }
    }

    public void addSaleItems(int saleId, List<SaleItem> items) throws SQLException {
        String insertSaleItemsQuery = "INSERT INTO SALEITEMS (SALE_ID, PROD_ID, QUANTITY, SUB_TOTAL) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(insertSaleItemsQuery);

        for (SaleItem item : items) {
            pstmt.setInt(1, saleId);
            pstmt.setInt(2, item.getProductId());
            pstmt.setInt(3, item.getQuantity());
            pstmt.setDouble(4, item.getSubtotal());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
    }

    public void updateProductStock(List<SaleItem> items) throws SQLException {
        String updateStockQuery = "UPDATE PRODUCTS SET QUANTITY_STOCK = QUANTITY_STOCK - ? WHERE PROD_ID = ?";
        PreparedStatement pstmt = connection.prepareStatement(updateStockQuery);

        for (SaleItem item : items) {
            pstmt.setInt(1, item.getQuantity());
            pstmt.setInt(2, item.getProductId());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
    }
}
