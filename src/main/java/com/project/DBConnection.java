package com.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnection {
    
    // Connection parameters
    private static final String URL = "jdbc:sqlserver://kerepekmaksu.database.windows.net:1433;";
    private static final String USER = "adminmaksu";
    private static final String PASSWORD = "maksupass@";
    private static final String DB_NAME = "KerepekMaksuDB";
    
    // Database connection method
    public static Connection getConnection() throws Exception {
        Connection conn = DriverManager.getConnection(
            URL + ";database=" + DB_NAME, USER, PASSWORD);
        return conn;
    }
    
    // Example of executing a query
    public static void queryDatabase() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
             
            String sql = "SELECT * FROM your_table";
            ResultSet rs = statement.executeQuery(sql);
            
            while (rs.next()) {
                System.out.println(rs.getString("your_column_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        queryDatabase();
    }
    
}

