package com.controller;

import com.manager.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String prodIdParam = request.getParameter("prodId");

        if (prodIdParam == null || prodIdParam.isEmpty()) {
            request.setAttribute("error", "Product ID is missing.");
            request.getRequestDispatcher("ViewProductServlet").forward(request, response);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            int prodId = Integer.parseInt(prodIdParam);

            // Update Product to inactive
            String updateProductSQL = "UPDATE Products SET PROD_STATUS = 'INACTIVE' WHERE PROD_ID = ?";

            try (PreparedStatement ps = conn.prepareStatement(updateProductSQL)) {
                ps.setInt(1, prodId);
                ps.executeUpdate();
            }

            // Delete Food or Drink based on category
            String deleteChildSQL;
            if (isFoodProduct(prodId, conn)) { // Assuming `isFoodProduct` is a helper method
                deleteChildSQL = "DELETE FROM Food WHERE PROD_ID = ?";
            } else {
                deleteChildSQL = "DELETE FROM Drink WHERE PROD_ID = ?";
            }

            try (PreparedStatement ps = conn.prepareStatement(deleteChildSQL)) {
                ps.setInt(1, prodId);
                ps.executeUpdate();
            }

            conn.commit(); // Commit transaction
            response.sendRedirect("ViewProductServlet");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error deleting product.");
            request.getRequestDispatcher("ViewProductServlet").forward(request, response);
        }
    }

    private boolean isFoodProduct(int prodId, Connection conn) throws SQLException {
        String query = "SELECT COUNT(*) FROM Food WHERE PROD_ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, prodId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
