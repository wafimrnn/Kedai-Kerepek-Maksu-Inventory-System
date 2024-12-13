package com.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.model.Product;

public class UpdateProductServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:sqlserver://maksukerepek.database.windows.net:1433;database=KedaiMaksuDB;";
    private static final String DB_USER = "maksuadmin";
    private static final String DB_PASSWORD = "Larvapass@";

    // Fetch product details for editing
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("product-id");  // Assuming product ID is passed as a request parameter
        Product product = null;

        // Establish the connection to the database
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT p.product_id, p.product_name, p.category, p.quantity, p.price, p.expiry_date, p.restock_level, " +
                         "f.weight, f.packaging_type, d.volume, p.image_path " +  // Include image_path in the query
                         "FROM products p " +
                         "LEFT JOIN food f ON p.product_id = f.product_id " +
                         "LEFT JOIN drink d ON p.product_id = d.product_id " +
                         "WHERE p.product_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, Integer.parseInt(productId));  // Set product ID in the query
                ResultSet resultSet = statement.executeQuery();
       
       if (resultSet.next()) {
    	   product = new Product(
    			    resultSet.getInt("product_id"),
    			    resultSet.getString("product_name"),
    			    resultSet.getString("category"),
    			    resultSet.getInt("quantity"),
    			    resultSet.getDouble("price"),
    			    resultSet.getDate("expiry_date"),
    			    resultSet.getInt("restock_level"),
    			    resultSet.getDouble("weight"),
    			    resultSet.getString("packaging_type"),
    			    resultSet.getDouble("volume"),
    			    resultSet.getString("image_path")
    			);
       }
   }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Send product to JSP for display
        request.setAttribute("product", product);
        request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
    }

    // Handle the update form submission
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("product-id");
        String productName = request.getParameter("product-name");
        String category = request.getParameter("category");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        double price = Double.parseDouble(request.getParameter("price"));
        String expiryDate = request.getParameter("expiry-date");
        int restockLevel = Integer.parseInt(request.getParameter("restock-level"));
        double weight = category.equals("snacks") ? Double.parseDouble(request.getParameter("weight")) : 0;
        String packagingType = category.equals("snacks") ? request.getParameter("packaging-type") : null;
        double volume = category.equals("drinks") ? Double.parseDouble(request.getParameter("volume")) : 0;
        String imagePath = request.getParameter("image-path");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Update products table
            String sql = "UPDATE products SET product_name = ?, category = ?, quantity = ?, price = ?, expiry_date = ?, restock_level = ?, image_path = ? WHERE product_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, productName);
                stmt.setString(2, category);
                stmt.setInt(3, quantity);
                stmt.setDouble(4, price);
                stmt.setString(5, expiryDate);
                stmt.setInt(6, restockLevel);
                stmt.setString(7, imagePath);
                stmt.setInt(8, Integer.parseInt(productId));
                stmt.executeUpdate();
            }

            // Update specific category tables
            if (category.equals("snacks")) {
                String foodSql = "UPDATE food SET weight = ?, packaging_type = ? WHERE product_id = ?";
                try (PreparedStatement foodStmt = conn.prepareStatement(foodSql)) {
                    foodStmt.setDouble(1, weight);
                    foodStmt.setString(2, packagingType);
                    foodStmt.setInt(3, Integer.parseInt(productId));
                    foodStmt.executeUpdate();
                }
            } else if (category.equals("drinks")) {
                String drinkSql = "UPDATE drink SET volume = ? WHERE product_id = ?";
                try (PreparedStatement drinkStmt = conn.prepareStatement(drinkSql)) {
                    drinkStmt.setDouble(1, volume);
                    drinkStmt.setInt(2, Integer.parseInt(productId));
                    drinkStmt.executeUpdate();
                }
            }

            request.setAttribute("message", "Product updated successfully!");
            request.setAttribute("messageType", "success");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "Error updating product: " + e.getMessage());
            request.setAttribute("messageType", "error");
        }

        request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
    }
}
