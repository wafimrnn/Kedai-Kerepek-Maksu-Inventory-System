package com.controller;

import jakarta.servlet.RequestDispatcher;
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

import com.dao.ProductDAO;
import com.model.Product;

public class ViewProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO productDAO = new ProductDAO();
        List<Product> productList = new ArrayList<>();

        try {
            productList = productDAO.getAllProducts();  // Fetch all products from DB
        } catch (SQLException e) {
            e.printStackTrace();  // Log or handle the exception as needed
            // Optionally, redirect to an error page or display a message
            request.setAttribute("errorMessage", "Error fetching product data.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("errorPage.jsp");
            dispatcher.forward(request, response);
            return;  // Exit the method if an error occurs
        }

        request.setAttribute("productList", productList);  // Set the list as request attribute
        RequestDispatcher dispatcher = request.getRequestDispatcher("ViewProduct.jsp");  // Forward to JSP
        dispatcher.forward(request, response);
    }
}
