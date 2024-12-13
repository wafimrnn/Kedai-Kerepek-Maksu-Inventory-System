package com.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.project.DBConnection;

/**
 * Servlet implementation class DeleteProductServlet
 */
@WebServlet("/DeleteProductServlet")
public class DeleteProductServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        String productType = request.getParameter("type"); // "food" or "drink"

        // Validate input
        if (idParam == null || productType == null) {
            response.sendRedirect("ViewProduct.jsp?error=invalidinput");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect("ViewProduct.jsp?error=invalidid");
            return;
        }

        // Determine the correct delete query
        String deleteQuery = ("food".equalsIgnoreCase(productType)) ? 
                "DELETE FROM Food WHERE productId = ?" : 
                ("drink".equalsIgnoreCase(productType)) ? 
                "DELETE FROM Drink WHERE productId = ?" : null;

        if (deleteQuery == null) {
            response.sendRedirect("ViewProduct.jsp?error=invalidtype");
            return;
        }

        // Perform deletion
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
            stmt.setInt(1, productId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                response.sendRedirect("InventoryList.jsp?success=true");
            } else {
                response.sendRedirect("InventoryList.jsp?error=notfound");
            }
        } catch (Exception e) {
            response.sendRedirect("InventoryList.jsp?error=sqlerror");
        }
    }
}