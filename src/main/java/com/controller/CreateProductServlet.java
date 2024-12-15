// CreateProductServlet.java
package com.controller;

import com.manager.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

@WebServlet("/CreateProductServlet")
public class CreateProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CreateProductServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve form data
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

        String insertProduct = "INSERT INTO Products (PROD_NAME, PROD_PRICE, QUANTITY_STOCK, " +
                               "RESTOCK_LEVEL, EXPIRY_DATE, IMAGE_PATH, PROD_STATUS) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String insertFood = "INSERT INTO Food (PROD_ID, PACKAGING_TYPE, WEIGHT) VALUES (?, ?, ?)";
        String insertDrink = "INSERT INTO Drink (PROD_ID, VOLUME) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement productStmt = conn.prepareStatement(insertProduct, Statement.RETURN_GENERATED_KEYS)) {
                productStmt.setString(1, prodName);
                productStmt.setDouble(2, prodPrice);
                productStmt.setInt(3, quantityStock);
                productStmt.setInt(4, restockLevel);
                productStmt.setDate(5, Date.valueOf(expiryDateStr));
                productStmt.setString(6, imagePath);
                productStmt.setString(7, prodStatus);

                int affectedRows = productStmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating product failed, no rows affected.");
                }

                // Retrieve the generated PROD_ID
                int prodId;
                try (ResultSet generatedKeys = productStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        prodId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating product failed, no ID obtained.");
                    }
                }

                // Insert into child table based on category
                if ("Food".equalsIgnoreCase(prodStatus)) {
                    try (PreparedStatement foodStmt = conn.prepareStatement(insertFood)) {
                        foodStmt.setInt(1, prodId);
                        foodStmt.setString(2, packagingType);
                        foodStmt.setDouble(3, weight);
                        foodStmt.executeUpdate();
                    }
                } else if ("Drink".equalsIgnoreCase(prodStatus)) {
                    try (PreparedStatement drinkStmt = conn.prepareStatement(insertDrink)) {
                        drinkStmt.setInt(1, prodId);
                        drinkStmt.setDouble(2, volume);
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
