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
                // Ensure you pass all necessary fields including imagePath
                product = new Product(
                    rs.getInt("productId"),
                    rs.getString("productName"),
                    rs.getString("category"),       // Make sure you're getting the right column name for category
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getDate("expiryDate"),       // Assuming expiryDate is stored in DB as Date
                    rs.getInt("restockLevel"),      // You should include restockLevel if you need it
                    rs.getDouble("weight"),         // Same for weight and packaging_type (if required)
                    rs.getString("packagingType"),
                    rs.getDouble("volume"),
                    rs.getString("imagePath")       // Make sure imagePath exists in the DB schema
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
                // Same as the getProductById method, ensure you're fetching all the required fields
                Product product = new Product(
                    rs.getInt("productId"),
                    rs.getString("productName"),
                    rs.getString("category"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getDate("expiryDate"),
                    rs.getInt("restockLevel"),
                    rs.getDouble("weight"),
                    rs.getString("packagingType"),
                    rs.getDouble("volume"),
                    rs.getString("imagePath")
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
        String query = "INSERT INTO products (productName, category, quantity, price, expiryDate, restockLevel, weight, packagingType, volume, imagePath) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getQuantity());
            stmt.setDouble(4, product.getPrice());
            stmt.setDate(5, new java.sql.Date(product.getExpiryDate().getTime())); // Convert java.util.Date to java.sql.Date
            stmt.setInt(6, product.getRestockLevel());
            stmt.setDouble(7, product.getWeight());
            stmt.setString(8, product.getPackagingType());
            stmt.setDouble(9, product.getVolume());
            stmt.setString(10, product.getImagePath());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a product
    public void updateProduct(Product product) {
        String query = "UPDATE products SET productName = ?, category = ?, quantity = ?, price = ?, expiryDate = ?, restockLevel = ?, weight = ?, packagingType = ?, volume = ?, imagePath = ? WHERE productId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getQuantity());
            stmt.setDouble(4, product.getPrice());
            stmt.setDate(5, new java.sql.Date(product.getExpiryDate().getTime()));
            stmt.setInt(6, product.getRestockLevel());
            stmt.setDouble(7, product.getWeight());
            stmt.setString(8, product.getPackagingType());
            stmt.setDouble(9, product.getVolume());
            stmt.setString(10, product.getImagePath());
            stmt.setInt(11, product.getProductId());

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
