package com.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

import com.dao.ProductDAO;

public class DeleteProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProductDAO productDAO;

    public DeleteProductServlet() {
        super();
        // Initialize ProductDAO
        productDAO = new ProductDAO();
    }

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

        try {
            // Delete the product based on type
            boolean isDeleted = productDAO.deleteProductById(productId, productType);

            if (isDeleted) {
                response.sendRedirect("InventoryList.jsp?success=true");
            } else {
                response.sendRedirect("InventoryList.jsp?error=notfound");
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log exception for debugging
            response.sendRedirect("InventoryList.jsp?error=sqlerror");
        }
    }
}