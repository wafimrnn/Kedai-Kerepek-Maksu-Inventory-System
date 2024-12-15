// DeleteProductServlet.java
package com.controller;

import com.manager.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;


public class DeleteProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DeleteProductServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int prodId = Integer.parseInt(request.getParameter("prodId"));

        String getStatus = "SELECT PROD_STATUS FROM Products WHERE PROD_ID = ?";
        String deleteFood = "DELETE FROM Food WHERE PROD_ID = ?";
        String deleteDrink = "DELETE FROM Drink WHERE PROD_ID = ?";
        String deleteProduct = "DELETE FROM Products WHERE PROD_ID = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            String prodStatus = null;
            try (PreparedStatement statusStmt = conn.prepareStatement(getStatus)) {
                statusStmt.setInt(1, prodId);
                try (ResultSet rs = statusStmt.executeQuery()) {
                    if (rs.next()) {
                        prodStatus = rs.getString("PROD_STATUS");
                    } else {
                        throw new SQLException("Product not found with PROD_ID: " + prodId);
                    }
                }
            }

            // Delete from child table based on category
            if ("Food".equalsIgnoreCase(prodStatus)) {
                try (PreparedStatement deleteFoodStmt = conn.prepareStatement(deleteFood)) {
                    deleteFoodStmt.setInt(1, prodId);
                    deleteFoodStmt.executeUpdate();
                }
            } else if ("Drink".equalsIgnoreCase(prodStatus)) {
                try (PreparedStatement deleteDrinkStmt = conn.prepareStatement(deleteDrink)) {
                    deleteDrinkStmt.setInt(1, prodId);
                    deleteDrinkStmt.executeUpdate();
                }
            }

            // Delete from Products table
            try (PreparedStatement deleteProductStmt = conn.prepareStatement(deleteProduct)) {
                deleteProductStmt.setInt(1, prodId);
                deleteProductStmt.executeUpdate();
            }

            conn.commit(); // Commit transaction

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception appropriately
            response.sendRedirect("error.jsp"); // Redirect to an error page
            return;
        }

        response.sendRedirect("ViewProductServlet"); // Redirect to product list
    }
}