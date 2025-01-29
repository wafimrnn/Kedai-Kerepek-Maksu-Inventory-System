package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.model.Food;

public class FoodDAO {

    // Insert food (ensure PROD_ID exists in PRODUCTS table)
    public void insertFood(Connection conn, Food food) throws SQLException {
        String sql = "INSERT INTO FOOD (PROD_ID, PACKAGING_TYPE, WEIGHT) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, food.getProdId()); // Ensure PROD_ID is valid
            ps.setString(2, food.getPackagingType());
            ps.setDouble(3, food.getWeight());
            ps.executeUpdate();
        }
    }
}
