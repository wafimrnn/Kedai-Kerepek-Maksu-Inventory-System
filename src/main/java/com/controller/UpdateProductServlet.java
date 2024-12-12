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

import com.model.Product;

/**
 * Servlet implementation class UpdateProductServlet
 */
@WebServlet("/UpdateProductServlet")
public class UpdateProductServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("product-id");
        Product product = null;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://maksukerepek.database.windows.net:1433;database=KedaiMaksuDB;", "maksuadmin", "Larvapass@")) {
            String sql = "SELECT product_id, product_name, category, quantity, price FROM products WHERE product_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, Integer.parseInt(productId));
                ResultSet resultSet = statement.executeQuery();
                
                if (resultSet.next()) {
                    String productName = resultSet.getString("product_name");
                    String category = resultSet.getString("category");
                    int quantity = resultSet.getInt("quantity");
                    double price = resultSet.getDouble("price");
                    
                    product = new Product(Integer.parseInt(productId), productName, "", quantity, category, price);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Pass the product details to the JSP page
        request.setAttribute("product", product);
        request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the updated product data from the form
        String productId = request.getParameter("product-id");
        String productName = request.getParameter("product-name");
        String category = request.getParameter("category");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        double price = Double.parseDouble(request.getParameter("price"));

        // Update the product in the database
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://maksukerepek.database.windows.net:1433;database=KedaiMaksuDB;", "maksuadmin", "Larvapass@")) {
            String sql = "UPDATE products SET product_name = ?, category = ?, quantity = ?, price = ? WHERE product_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, productName);
                statement.setString(2, category);
                statement.setInt(3, quantity);
                statement.setDouble(4, price);
                statement.setInt(5, Integer.parseInt(productId));
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Redirect back to the product view page after update
        response.sendRedirect("ViewProductServlet");
    }
}