package com.controller;

import com.model.Product;
import com.manager.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ViewProductServlet")
public class ViewProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ViewProductServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> productList = new ArrayList<>();

        String query = "SELECT p.PROD_ID, p.PROD_NAME, p.PROD_PRICE, p.QUANTITY_STOCK, " +
                       "p.RESTOCK_LEVEL, p.EXPIRY_DATE, p.IMAGE_PATH, p.PROD_STATUS, " +
                       "f.PACKAGING_TYPE, f.WEIGHT, d.VOLUME " +
                       "FROM Products p " +
                       "LEFT JOIN Food f ON p.PROD_ID = f.PROD_ID " +
                       "LEFT JOIN Drink d ON p.PROD_ID = d.PROD_ID";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product;
                String status = rs.getString("PROD_STATUS");
                if ("Food".equalsIgnoreCase(status)) {
                    product = new com.model.Food();
                    ((com.model.Food) product).setPackagingType(rs.getString("PACKAGING_TYPE"));
                    ((com.model.Food) product).setWeight(rs.getDouble("WEIGHT"));
                } else if ("Drink".equalsIgnoreCase(status)) {
                    product = new com.model.Drink();
                    ((com.model.Drink) product).setVolume(rs.getDouble("VOLUME"));
                } else {
                    product = new Product();
                }

                product.setProdId(rs.getInt("PROD_ID"));
                product.setProdName(rs.getString("PROD_NAME"));
                product.setProdPrice(rs.getDouble("PROD_PRICE"));
                product.setQuantityStock(rs.getInt("QUANTITY_STOCK"));
                product.setRestockLevel(rs.getInt("RESTOCK_LEVEL"));
                product.setExpiryDate(rs.getDate("EXPIRY_DATE"));
                product.setImagePath(rs.getString("IMAGE_PATH"));
                product.setProdStatus(status);

                productList.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }

        request.setAttribute("productList", productList);
        request.getRequestDispatcher("ViewProduct.jsp").forward(request, response);
    }
}
