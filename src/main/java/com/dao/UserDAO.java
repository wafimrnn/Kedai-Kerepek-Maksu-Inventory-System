package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.manager.DBConnection;
import com.model.User;

public class UserDAO {

    // Method to create an owner
	public boolean createOwner(String username, String password, String phone, String address) {
	    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12)); // Hash password

	    String sql = "INSERT INTO USERS (USER_NAME, USER_ROLE, USER_PASS, USER_PHONE, USER_ADDRESS, ACC_STATUS) " +
	                 "VALUES (?, 'OWNER', ?, ?, ?, 'ACTIVE')";

	    try (Connection connection = DBConnection.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        preparedStatement.setString(1, username);
	        preparedStatement.setString(2, hashedPassword); // Store hashed password
	        preparedStatement.setString(3, phone);
	        preparedStatement.setString(4, address);

	        return preparedStatement.executeUpdate() > 0;
	    } catch (SQLException e) {
	        System.err.println("Error creating owner: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return false;
	}

    // Method to log in a user
	public User loginUser(String username, String password) {
	    String query = "SELECT * FROM USERS WHERE USER_NAME = ?";
	    try (Connection connection = DBConnection.getConnection();
	         PreparedStatement statement = connection.prepareStatement(query)) {

	        statement.setString(1, username);
	        ResultSet resultSet = statement.executeQuery();

	        if (resultSet.next()) {
	            String storedPasswordHash = resultSet.getString("USER_PASS"); // Get hashed password

	            // Compare entered password with hashed password
	            if (BCrypt.checkpw(password, storedPasswordHash)) {
	                User user = new User();
	                user.setId(resultSet.getInt("USER_ID"));
	                user.setName(resultSet.getString("USER_NAME"));
	                user.setRole(resultSet.getString("USER_ROLE"));
	                return user; // Return user if password matches
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null; // Return null if authentication fails
	}

    // Method to create a staff account
    public boolean createStaff(String username, String password, String phone, String address, int ownerId) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12)); // Hash password

        String query = "INSERT INTO USERS (USER_NAME, USER_ROLE, USER_PASS, USER_PHONE, USER_ADDRESS, ACC_STATUS, OWNER_ID) " +
                       "VALUES (?, 'STAFF', ?, ?, ?, 'ACTIVE', ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword); // Store hashed password
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.setInt(5, ownerId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating staff: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Method to check if the username is already taken
    public boolean isUsernameTaken(String username) {
        String query = "SELECT COUNT(*) FROM USERS WHERE USER_NAME = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Method to update a user's account
    public boolean updateUserAccount(int userId, String username, String phone, String address, String role, String status) {
        String sql = "UPDATE USERS SET USER_NAME = ?, USER_PHONE = ?, USER_ADDRESS = ?, USER_ROLE = ?, ACC_STATUS = ? WHERE USER_ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, phone);
            stmt.setString(3, address);
            stmt.setString(4, role);
            stmt.setString(5, status);
            stmt.setInt(6, userId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user account: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Method to get a user by their ID
    public User getUserById(int userId) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("USER_ID"));
                user.setName(rs.getString("USER_NAME"));
                user.setRole(rs.getString("USER_ROLE"));
                user.setPhone(rs.getString("USER_PHONE"));
                user.setAddress(rs.getString("USER_ADDRESS"));
                user.setAccStatus(rs.getString("ACC_STATUS"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    // Method to get staff members by owner ID
    public List<User> getStaffByOwnerId(int ownerId) {
        String sql = "SELECT * FROM USERS WHERE OWNER_ID = ? AND USER_ROLE = 'STAFF'";
        List<User> staffList = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("USER_ID"));
                user.setName(rs.getString("USER_NAME"));
                user.setRole(rs.getString("USER_ROLE"));
                user.setPhone(rs.getString("USER_PHONE"));
                user.setAddress(rs.getString("USER_ADDRESS"));
                staffList.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving staff by owner ID: " + e.getMessage());
            e.printStackTrace();
        }
        return staffList;
    }

    // Method to update account status
    public boolean updateAccountStatus(int staffId, String newStatus) {
        String sql = "UPDATE USERS SET ACC_STATUS = ? WHERE USER_ID = ?";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, staffId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Returns true if the update was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
