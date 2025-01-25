package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import com.manager.DBConnection;
import com.model.User;

public class UserDAO {
	public boolean createOwner(String username, String password, String phone, String address) {
	    String sql = "INSERT INTO USERS (USER_NAME, USER_ROLE, USER_PASS, USER_PHONE, USER_ADDRESS, ACC_STATUS) VALUES (?, 'OWNER', ?, ?, ?, 'ACTIVE')";
	    try (Connection connection = DBConnection.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        // Hash the password using BCrypt
	        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

	        // Set the parameters
	        preparedStatement.setString(1, username);
	        preparedStatement.setString(2, hashedPassword);
	        preparedStatement.setString(3, phone);
	        preparedStatement.setString(4, address);

	        // Execute the update
	        int rowsAffected = preparedStatement.executeUpdate();

	        // If rows were inserted, the account creation is successful
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	    	System.out.println("SQL Error: " + e.getMessage());
	        e.printStackTrace(); // Log the error for debugging
	    }
	    return false; // Return false if an exception occurs or no rows were inserted
	}

    public User loginUser(String username, String password) {
        String query = "SELECT * FROM USERS WHERE USER_NAME = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && checkPassword(password, rs.getString("USER_PASS"))) {
                User user = new User();
                user.setId(rs.getInt("USER_ID"));
                user.setName(rs.getString("USER_NAME"));
                user.setRole(rs.getString("USER_ROLE"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createStaff(String username, String password, String phone, String address, int ownerId) {
        String query = "INSERT INTO USERS (USER_NAME, USER_ROLE, USER_PASS, USER_PHONE, USER_ADDRESS, ACC_STATUS, OWNER_ID) VALUES (?, 'STAFF', ?, ?, ?, 'ACTIVE', ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, encryptPassword(password));
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.setInt(5, ownerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean isUsernameTaken(String username) {
        String query = "SELECT COUNT(*) FROM USERS WHERE USER_NAME = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Return true if the count is greater than 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String encryptPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt()); // Use BCrypt for encryption.
    }

    private boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword); // Compare passwords.
    }
    
    private static final String UPDATE_ACCOUNT_QUERY = "UPDATE USERS SET USER_PHONE = ?, USER_ADDRESS = ? WHERE USER_ID = ?";

    public boolean updateUserAccount(int userId, String phone, String address) {
        boolean isUpdated = false;
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_ACCOUNT_QUERY);
            statement.setString(1, phone);
            statement.setString(2, address);
            statement.setInt(3, userId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                isUpdated = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUpdated;
    }
    
    private static final String GET_USER_BY_ID_QUERY = "SELECT * FROM USERS WHERE USER_ID = ?";

    public User getUserById(int userId) {
        User user = null;
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(GET_USER_BY_ID_QUERY);
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Create a User object and populate it with data from the result set
                user = new User();
                user.setId(resultSet.getInt("USER_ID"));
                user.setName(resultSet.getString("USER_NAME"));
                user.setRole(resultSet.getString("USER_ROLE"));
                user.setPhone(resultSet.getString("USER_PHONE"));
                user.setAddress(resultSet.getString("USER_ADDRESS"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}