package com.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.model.Product;

@WebServlet("/ViewProductServlet")
public class ViewProductServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:sqlserver://maksukerepek.database.windows.net:1433;database=KedaiMaksuDB;";
    private static final String DB_USER = "maksuadmin";
    private static final String DB_PASSWORD = "Larvapass@";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> products = new ArrayList<>();

        String query = "SELECT product_id, product_name, image_path, category, quantity, price FROM products";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("image_path"),
                    rs.getInt("quantity"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    null // Assuming expiry date is not fetched here
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging this instead
        }

        request.setAttribute("products", products);
        request.getRequestDispatcher("ViewProduct.jsp").forward(request, response);
    }
}
