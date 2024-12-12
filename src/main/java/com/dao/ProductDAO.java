package com.dao;

import com.model.Product;
import com.project.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Method to get a product by its ID
    public Product getProductById(int productId) {
        Product product = null;
        String query = "SELECT * FROM products WHERE productId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                product = new Product(
                    rs.getInt("productId"),
                    rs.getString("productName"),
                    rs.getString("imagePath"),
                    rs.getInt("quantity"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getDate("expiryDate") // Assuming expiryDate is stored in DB
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }

    // Method to get all products
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("productId"),
                    rs.getString("productName"),
                    rs.getString("imagePath"),
                    rs.getInt("quantity"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getDate("expiryDate") // Assuming expiryDate is stored in DB
                );
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    // Method to add a new product
    public void addProduct(Product product) {
        String query = "INSERT INTO products (productName, imagePath, quantity, category, price, expiryDate) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getImagePath());
            stmt.setInt(3, product.getQuantity());
            stmt.setString(4, product.getCategory());
            stmt.setDouble(5, product.getPrice());
            stmt.setDate(6, new java.sql.Date(product.getExpiryDate().getTime())); // Convert java.util.Date to java.sql.Date

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a product
    public void updateProduct(Product product) {
        String query = "UPDATE products SET productName = ?, imagePath = ?, quantity = ?, category = ?, price = ?, expiryDate = ? WHERE productId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getImagePath());
            stmt.setInt(3, product.getQuantity());
            stmt.setString(4, product.getCategory());
            stmt.setDouble(5, product.getPrice());
            stmt.setDate(6, new java.sql.Date(product.getExpiryDate().getTime()));
            stmt.setInt(7, product.getProductId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a product
    public void deleteProduct(int productId) {
        String query = "DELETE FROM products WHERE productId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
