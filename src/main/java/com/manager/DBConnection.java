package com.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String DB_URL = "jdbc:sqlserver://kerepekmaksu.database.windows.net:1433;database=KerepekMaksuDB";  // Replace with your actual DB URL
    private static final String DB_USER = "maksuadmin";  // Replace with your actual DB username
    private static final String DB_PASSWORD = "Larva@12";  // Replace with your actual DB password
    
    static {
        try {
            // Load the JDBC driver (example for SQL Server)
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to get a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
