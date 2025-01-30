package com.controller.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.dao.ProductDAO;
import com.manager.DBConnection;
import com.model.Product;
import com.model.User;

public class UpdateProductStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String prodId = request.getParameter("prodId");

        if (prodId != null) {
            ProductDAO productDAO = new ProductDAO();
            boolean success = productDAO.updateProductStatus(Integer.parseInt(prodId), "ACTIVE");

            if (success) {
                request.getSession().setAttribute("successMessage", "Product status updated successfully.");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to update product status.");
            }
        }

        response.sendRedirect("ViewProductServlet"); // Redirect back to the product list page
    }
}