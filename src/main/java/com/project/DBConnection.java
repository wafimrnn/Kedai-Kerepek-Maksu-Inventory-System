package com.project;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static Connection connection;

    static {
        try {
            // Load properties from dbconfig.properties
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/dbconfig.properties");
            props.load(fis);

            // Get database credentials from properties
            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            // Load the Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establish the connection
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection established successfully!");

        } catch (IOException e) {
            System.err.println("Error reading dbconfig.properties: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Oracle JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error establishing database connection: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }
    
}