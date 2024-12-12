package com.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    // Connection parameters
    private static final String URL = "jdbc:sqlserver://maksukerepek.database.windows.net:1433;database=KedaiMaksuDB;";
    private static final String USER = "maksuadmin";
    private static final String PASSWORD = "Larvapass@";
    private static final String DB_NAME = "KerepekMaksuDB";
    
    // Database connection method
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            URL + ";database=" + DB_NAME, USER, PASSWORD);
    }
}