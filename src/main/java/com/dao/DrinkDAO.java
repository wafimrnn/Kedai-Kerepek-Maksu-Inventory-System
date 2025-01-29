package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.model.Drink;

public class DrinkDAO {

    // Insert drink (ensure PROD_ID exists in PRODUCTS table)
    public void insertDrink(Connection conn, Drink drink) throws SQLException {
        String sql = "INSERT INTO DRINK (PROD_ID, VOLUME) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, drink.getProdId()); // Ensure PROD_ID is valid
            ps.setDouble(2, drink.getVolume());
            ps.executeUpdate();
        }
    }
}
