package com.controller.product;

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

        int prodId;
        try {
            prodId = Integer.parseInt(prodIdParam);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid Product ID format.");
            request.getRequestDispatcher("ViewProductServlet").forward(request, response);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            if (!updateProductStatusToInactive(conn, prodId)) {
                request.setAttribute("error", "Failed to update product status.");
                conn.rollback();
                request.getRequestDispatcher("ViewProductServlet").forward(request, response);
                return;
            }

            conn.commit(); // Commit transaction
            response.sendRedirect("ViewProductServlet");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error deleting product.");
            request.getRequestDispatcher("ViewProductServlet").forward(request, response);
        }
    }

    private boolean updateProductStatusToInactive(Connection conn, int prodId) throws SQLException {
        String sql = "UPDATE Products SET PROD_STATUS = 'INACTIVE' WHERE PROD_ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prodId);
            return ps.executeUpdate() > 0;
        }
    }
}

