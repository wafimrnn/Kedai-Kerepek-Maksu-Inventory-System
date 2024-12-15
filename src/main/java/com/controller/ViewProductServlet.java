package com.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.dao.ProductDAO;
import com.model.Product;

public class ViewProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProductDAO productDAO;

    public ViewProductServlet() {
        super();
        // Initialize DAO (consider making DAO a singleton to improve performance)
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Fetch all products using ProductDAO
            List<Product> productList = productDAO.getAllProducts();
            request.setAttribute("productList", productList);  // Set the product list in the request scope

            // Forward to the JSP page for rendering the product list
            RequestDispatcher dispatcher = request.getRequestDispatcher("ViewProduct.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();  // Log the exception (consider using a logging framework)
            // Redirect to an error page with a meaningful message
            request.setAttribute("errorMessage", "Error fetching product data: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("errorPage.jsp");
            dispatcher.forward(request, response);
        }
    }
}
