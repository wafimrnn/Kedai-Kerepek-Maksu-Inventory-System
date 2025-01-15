package com.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static String dbUrl = System.getenv("AZURE_SQL_CONNECTIONSTRING");
    
    static {
        try {
            // Load the JDBC driver (example for SQL Server)
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dbUrl == null || dbUrl.isEmpty()) {
            throw new IllegalStateException("Database connection string is not set in environment variables.");
        }
        return DriverManager.getConnection(dbUrl);
    }
    
}