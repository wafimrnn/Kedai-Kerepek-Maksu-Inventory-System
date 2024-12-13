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
import java.util.ArrayList;
import java.util.List;

import com.model.Product;

public class ViewProductServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:sqlserver://maksukerepek.database.windows.net:1433;database=KedaiMaksuDB;";
    private static final String DB_USER = "maksuadmin";
    private static final String DB_PASSWORD = "Larvapass@";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> products = new ArrayList<>();

        String query = "SELECT PROD_ID, PROD_NAME, IMAGE_PATH QUANTITY_STOCK, PROD_PRICE FROM PRODUCTS";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Retrieve data from ResultSet
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                String category = rs.getString("category");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                String imagePath = rs.getString("image_path");

                // Create Product object with default/null values for missing fields
                Product product = new Product(
                    productId, productName, category, quantity, price, null, // expiryDate
                    0, // restockLevel (default)
                    0.0, // weight (default)
                    null, // packagingType (default)
                    0.0, // volume (default)
                    imagePath
                );

                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging this instead
        }

        // Attach products list to request and forward to JSP
        request.setAttribute("products", products);
        request.getRequestDispatcher("ViewProduct.jsp").forward(request, response);
    }
}
