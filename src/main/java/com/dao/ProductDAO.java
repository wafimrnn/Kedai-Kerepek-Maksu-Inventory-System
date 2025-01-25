package com.dao;

import com.model.Drink;
import com.model.Food;
import com.model.Product;
import com.manager.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
	
	public List<Product> getAllActiveProducts() {
	    List<Product> products = new ArrayList<>();

	    String sql = "SELECT p.PROD_ID, p.PROD_NAME, p.PROD_PRICE, p.QUANTITY_STOCK, p.IMAGE_PATH, " +
	                 "       f.PACKAGING_TYPE, f.WEIGHT, d.VOLUME " +
	                 "FROM Products p " +
	                 "LEFT JOIN Food f ON p.PROD_ID = f.PROD_ID " +
	                 "LEFT JOIN Drink d ON p.PROD_ID = d.PROD_ID " +
	                 "WHERE p.PROD_STATUS = 'ACTIVE'";

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            // Fetch the image path and apply fallback if necessary
	            String imagePath = rs.getString("IMAGE_PATH");
	            if (imagePath == null || imagePath.isEmpty()) {
	                imagePath = "img/default-image.jpg"; // Default image if none provided
	            }

	            if (rs.getString("PACKAGING_TYPE") != null) {
	                Food food = new Food();
	                food.setProdId(rs.getInt("PROD_ID"));
	                food.setProdName(rs.getString("PROD_NAME"));
	                food.setProdPrice(rs.getDouble("PROD_PRICE"));
	                food.setQuantityStock(rs.getInt("QUANTITY_STOCK"));
	                food.setImagePath(imagePath);  // Use the fallback image path
	                food.setPackagingType(rs.getString("PACKAGING_TYPE"));
	                food.setWeight(rs.getDouble("WEIGHT"));
	                products.add(food);
	            } else if (rs.getDouble("VOLUME") > 0) {
	                Drink drink = new Drink();
	                drink.setProdId(rs.getInt("PROD_ID"));
	                drink.setProdName(rs.getString("PROD_NAME"));
	                drink.setProdPrice(rs.getDouble("PROD_PRICE"));
	                drink.setQuantityStock(rs.getInt("QUANTITY_STOCK"));
	                drink.setImagePath(imagePath);  // Use the fallback image path
	                drink.setVolume(rs.getDouble("VOLUME"));
	                products.add(drink);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return products;
	}
    
    //create product
    public int insertProduct(String name, double price, int quantity, int restockLevel, String expiryDate, String imagePath) throws SQLException {
        String sql = "INSERT INTO Products (PROD_NAME, PROD_PRICE, QUANTITY_STOCK, RESTOCK_LEVEL, EXPIRY_DATE, IMAGE_PATH, PROD_STATUS) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, quantity);
            ps.setInt(4, restockLevel);
            ps.setString(5, expiryDate);
            ps.setString(6, imagePath);
            ps.setString(7, "ACTIVE");

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return generated product ID
                }
            }
        }
        throw new SQLException("Failed to insert product and retrieve generated ID.");
    }
    
    public static void updateProduct(Product product) throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            String updateSQL = "UPDATE Products SET PROD_NAME = ?, PROD_PRICE = ?, QUANTITY_STOCK = ?, IMAGE_PATH = ? WHERE PROD_ID = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSQL)) {
                ps.setString(1, product.getProdName());
                ps.setDouble(2, product.getProdPrice());
                ps.setInt(3, product.getQuantityStock());
                ps.setString(4, product.getImagePath());
                ps.setInt(5, product.getProdId());
                ps.executeUpdate();
            }

            // Update specific fields for Food or Drink
            if (product instanceof Food) {
                Food food = (Food) product;
                String updateFoodSQL = "UPDATE Foods SET PACKAGING_TYPE = ?, WEIGHT = ? WHERE PROD_ID = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateFoodSQL)) {
                    ps.setString(1, food.getPackagingType());
                    ps.setDouble(2, food.getWeight());
                    ps.setInt(3, food.getProdId());
                    ps.executeUpdate();
                }
            } else if (product instanceof Drink) {
                Drink drink = (Drink) product;
                String updateDrinkSQL = "UPDATE Drinks SET VOLUME = ? WHERE PROD_ID = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateDrinkSQL)) {
                    ps.setDouble(1, drink.getVolume());
                    ps.setInt(2, drink.getProdId());
                    ps.executeUpdate();
                }
            }
        }
    }
    
}