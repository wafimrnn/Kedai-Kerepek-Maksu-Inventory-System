package com.dao;

import com.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database"; // Update with your database details
    private static final String DB_USERNAME = "your_username"; // Database username
    private static final String DB_PASSWORD = "your_password"; // Database password

    // Method to get a product by its ID
    public Product getProductById(int productId) {
        Product product = null;
        String query = "SELECT * FROM products WHERE productId = ?"; // Update with your actual table name and column names

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Assuming your Product table has columns: productId, productName, imagePath, price, quantity, category
                product = new Product(
                    rs.getInt("productId"),
                    rs.getString("productName"),
                    rs.getString("imagePath"),
                    rs.getInt("quantity"),
                    rs.getString("category"),
                    rs.getDouble("price")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception properly
        }

        return product;
    }

    // Method to get all products (optional, for listing products)
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products"; // Update with your actual table name and column names

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("productId"),
                    rs.getString("productName"),
                    rs.getString("imagePath"),
                    rs.getInt("quantity"),
                    rs.getString("category"),
                    rs.getDouble("price")
                );
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception properly
        }

        return products;
    }

    // Additional methods like add, update, delete can be added here
}
