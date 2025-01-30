package com.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_URL = "jdbc:sqlserver://kerepekmaksu.database.windows.net:1433;"
                                       + "database=KerepekMaksu;"
                                       + "user=maksuadmin@kerepekmaksu;"
                                       + "password=Larvapass@;"
                                       + "encrypt=true;"
                                       + "trustServerCertificate=false;"
                                       + "hostNameInCertificate=*.database.windows.net;"
                                       + "loginTimeout=30;";

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Connection successful!");
        } catch (SQLException e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
        }
    }
}
