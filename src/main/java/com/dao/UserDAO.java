package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.manager.DBConnection;

public class UserDAO {
    public boolean validateUser(String username, String password) throws SQLException {
        String query = "SELECT 1 FROM USERS WHERE USER_NAME = ? AND USER_PASS = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean isUsernameTaken(String username) throws SQLException {
        String query = "SELECT 1 FROM USERS WHERE USER_NAME = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean createUser(String username, String password, String phone, String role, String address, Integer ownerId) throws SQLException {
        String query = "INSERT INTO USERS (USER_NAME, USER_PASS, USER_PHONE, USER_ROLE, USER_ADDRESS, OWNER_ID) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, phone);
            stmt.setString(4, role);
            stmt.setString(5, address);
            
            // Handle ownerId being null
            if (ownerId == null) {
                stmt.setNull(6, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(6, ownerId);
            }
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public int countUsers() throws Exception {
        String query = "SELECT COUNT(*) AS total FROM USERS";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        }
        return 0;
    }
}