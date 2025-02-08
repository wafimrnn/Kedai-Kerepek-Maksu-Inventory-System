package com.dao;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import com.manager.DBConnection;
import com.model.Sale;
import com.model.SaleItem;

public class SaleDAO {

    public int insertSale(Sale sale) throws Exception {
        String query = "INSERT INTO SALES (sale_Date, total_Amount, payment_Method, user_Id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, Date.valueOf(sale.getSaleDate()));
            ps.setDouble(2, sale.getTotalAmount());
            ps.setString(3, sale.getPaymentMethod());
            ps.setInt(4, sale.getUserId());
            ps.executeUpdate();

            // Retrieve the generated sale_Id
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new Exception("Failed to retrieve the inserted sale ID.");
                }
            }
        }
    }

    public void insertSaleItems(Connection conn, int saleId, List<SaleItem> saleItems) throws SQLException {
        String query = "INSERT INTO SaleItems (sale_Id, prod_Id, quantity, sub_Total) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            for (SaleItem item : saleItems) {
                if (!isSaleItemExists(conn, saleId, item.getProdId())) {
                    ps.setInt(1, saleId);
                    ps.setInt(2, item.getProdId());
                    ps.setInt(3, item.getQuantity());
                    ps.setDouble(4, item.getSubTotal());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }

    private boolean isSaleItemExists(Connection conn, int saleId, int prodId) throws SQLException {
        String query = "SELECT COUNT(*) FROM SaleItems WHERE sale_Id = ? AND prod_Id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, saleId);
            ps.setInt(2, prodId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public List<Sale> getSalesByDate(Date saleDate) {
        List<Sale> salesList = new ArrayList<>();
        String query = "SELECT sale_Id, sale_Date, total_Amount, payment_Method, user_Id FROM Sales WHERE SALE_DATE = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setDate(1, saleDate);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Sale sale = new Sale();
                    sale.setSaleId(resultSet.getInt("sale_Id"));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    sale.setSaleDate(dateFormat.format(resultSet.getDate("sale_Date")));
                    sale.setTotalAmount(resultSet.getBigDecimal("total_Amount").doubleValue());
                    sale.setPaymentMethod(resultSet.getString("payment_Method"));
                    sale.setUserId(resultSet.getInt("user_Id"));
                    salesList.add(sale);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salesList;
    }

    public boolean checkSaleItemExists(int saleId, int prodId) throws Exception {
        String query = "SELECT COUNT(*) FROM SaleItems WHERE SALE_ID = ? AND PROD_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, saleId);
            ps.setInt(2, prodId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
    
    public List<Sale> getSalesReport(String startDate, String endDate) {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM SALES WHERE SALE_DATE BETWEEN ? AND ?";
        
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, startDate);
            stmt.setString(2, endDate);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Sale sale = new Sale(
                    rs.getInt("SALE_ID"),
                    rs.getString("SALE_DATE"),
                    rs.getDouble("TOTAL_AMOUNT"),
                    rs.getString("PAYMENT_METHOD")
                );
                sales.add(sale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sales;
    }
    
    public Sale getSaleWithItems(int saleId) {
        Sale sale = null;
        String saleQuery = "SELECT sale_Id, sale_Date, total_Amount, payment_Method, user_Id FROM Sales WHERE sale_Id = ?";
        String itemQuery = "SELECT si.sale_Id, si.prod_Id, p.prod_Name, si.quantity, si.sub_Total " +
                           "FROM SaleItems si " +
                           "JOIN Products p ON si.prod_Id = p.prod_Id " +
                           "WHERE si.sale_Id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement saleStmt = conn.prepareStatement(saleQuery);
             PreparedStatement itemStmt = conn.prepareStatement(itemQuery)) {

            // Get sale details
            saleStmt.setInt(1, saleId);
            ResultSet saleRs = saleStmt.executeQuery();
            if (saleRs.next()) {
                sale = new Sale();
                sale.setSaleId(saleRs.getInt("sale_Id"));
                sale.setSaleDate(saleRs.getString("sale_Date"));
                sale.setTotalAmount(saleRs.getDouble("total_Amount"));
                sale.setPaymentMethod(saleRs.getString("payment_Method"));
                sale.setUserId(saleRs.getInt("user_Id"));
            }

            // Get sale items
            if (sale != null) {
                List<SaleItem> items = new ArrayList<>();
                itemStmt.setInt(1, saleId);
                ResultSet itemRs = itemStmt.executeQuery();
                while (itemRs.next()) {
                    SaleItem item = new SaleItem();
                    item.setSaleId(itemRs.getInt("sale_Id"));
                    item.setProdId(itemRs.getInt("prod_Id"));
                    item.setProdName(itemRs.getString("prod_Name"));  // Retrieve prodName
                    item.setQuantity(itemRs.getInt("quantity"));
                    item.setSubTotal(itemRs.getDouble("sub_Total"));
                    items.add(item);
                }
                sale.setSaleItems(items);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sale;
    }
}
