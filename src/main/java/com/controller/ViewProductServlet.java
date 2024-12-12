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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://maksukerepek.database.windows.net:1433;database=KedaiMaksuDB;", "maksuadmin", "Larvapass@")) {
            String sql = "SELECT id, product_name, image_path, price, description FROM products";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                
                while (resultSet.next()) {
                	int productId = resultSet.getInt("product_id");
                    String productName = resultSet.getString("product_name");
                    String imagePath = resultSet.getString("image_path");
                    String category = resultSet.getString("category");
                    int quantity = resultSet.getInt("quantity");
                    double price = resultSet.getDouble("price");

                    products.add(new Product(productId, productName, imagePath, quantity, category, price));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Forward the list of products to the JSP
        request.setAttribute("products", products);
        request.getRequestDispatcher("ViewProduct.jsp").forward(request, response);
    }
}