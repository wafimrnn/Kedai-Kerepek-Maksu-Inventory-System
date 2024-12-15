// UpdateProductServlet.java
package com.controller;

import com.manager.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

@WebServlet("/UpdateProductServlet")
public class UpdateProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UpdateProductServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve form data
        int prodId = Integer.parseInt(request.getParameter("prodId"));
        String prodName = request.getParameter("prodName");
        double prodPrice = Double.parseDouble(request.getParameter("prodPrice"));
        int quantityStock = Integer.parseInt(request.getParameter("quantityStock"));
        int restockLevel = Integer.parseInt(request.getParameter("restockLevel"));
        String expiryDateStr = request.getParameter("expiryDate"); // Expecting format 'yyyy-MM-dd'
        String imagePath = request.getParameter("imagePath");
        String prodStatus = request.getParameter("prodStatus"); // "Food" or "Drink"

        // Additional fields based on category
        String packagingType = null;
        Double weight = null;
        Double volume = null;

        if ("Food".equalsIgnoreCase(prodStatus)) {
            packagingType = request.getParameter("packagingType");
            weight = Double.parseDouble(request.getParameter("weight"));
        } else if ("Drink".equalsIgnoreCase(prodStatus)) {
            volume = Double.parseDouble(request.getParameter("volume"));
        }

        String updateProduct = "UPDATE Products SET PROD_NAME = ?, PROD_PRICE = ?, QUANTITY_STOCK = ?, " +
                               "RESTOCK_LEVEL = ?, EXPIRY_DATE = ?, IMAGE_PATH = ?, PROD_STATUS = ? " +
                               "WHERE PROD_ID = ?";

        String updateFood = "UPDATE Food SET PACKAGING_TYPE = ?, WEIGHT = ? WHERE PROD_ID = ?";
        String updateDrink = "UPDATE Drink SET VOLUME = ? WHERE PROD_ID = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement productStmt = conn.prepareStatement(updateProduct)) {
                productStmt.setString(1, prodName);
                productStmt.setDouble(2, prodPrice);
                productStmt.setInt(3, quantityStock);
                productStmt.setInt(4, restockLevel);
                productStmt.setDate(5, Date.valueOf(expiryDateStr));
                productStmt.setString(6, imagePath);
                productStmt.setString(7, prodStatus);
                productStmt.setInt(8, prodId);

                productStmt.executeUpdate();

                // Update child tables based on category
                if ("Food".equalsIgnoreCase(prodStatus)) {
                    try (PreparedStatement foodStmt = conn.prepareStatement(updateFood)) {
                        foodStmt.setString(1, packagingType);
                        foodStmt.setDouble(2, weight);
                        foodStmt.setInt(3, prodId);
                        foodStmt.executeUpdate();
                    }
                } else if ("Drink".equalsIgnoreCase(prodStatus)) {
                    try (PreparedStatement drinkStmt = conn.prepareStatement(updateDrink)) {
                        drinkStmt.setDouble(1, volume);
                        drinkStmt.setInt(2, prodId);
                        drinkStmt.executeUpdate();
                    }
                }

                conn.commit(); // Commit transaction
            } catch (SQLException e) {
                conn.rollback(); // Rollback transaction on error
                e.printStackTrace();
                // Handle exception appropriately
                response.sendRedirect("error.jsp"); // Redirect to an error page
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp"); // Redirect to an error page
            return;
        }

        response.sendRedirect("ViewProductServlet"); // Redirect to product list
    }
}
