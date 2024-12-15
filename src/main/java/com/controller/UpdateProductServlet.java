package com.servlet;

import com.dao.ProductDAO;
import com.model.Food;
import com.model.Drink;
import com.model.Product;
import java.io.*;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;

public class UpdateProductServlet extends HttpServlet {
    
    private ProductDAO productDAO;

    // Initialize DAO
    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();  // Instantiate ProductDAO
    }

    // Handle the update request
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch product details from the request
        int productId = Integer.parseInt(request.getParameter("productId"));
        String productName = request.getParameter("productName");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantityStock = Integer.parseInt(request.getParameter("quantityStock"));
        int restockLevel = Integer.parseInt(request.getParameter("restockLevel"));
        String expiryDate = request.getParameter("expiryDate");
        String productStatus = request.getParameter("productStatus");
        String category = request.getParameter("category");  // 'food' or 'drink'

        Product product = null;
        
        if ("food".equalsIgnoreCase(category)) {
            // Fetch food-specific details
            double weight = Double.parseDouble(request.getParameter("weight"));
            String packagingType = request.getParameter("packagingType");
            
            product = new Food();
            ((Food) product).setWeight(weight);
            ((Food) product).setPackagingType(packagingType);
        } else if ("drink".equalsIgnoreCase(category)) {
            // Fetch drink-specific details
            double volume = Double.parseDouble(request.getParameter("volume"));
            
            product = new Drink();
            ((Drink) product).setVolume(volume);
        }
        
        if (product != null) {
            // Set common properties
            product.setProductId(productId);
            product.setProductName(productName);
            product.setPrice(price);
            product.setQuantityStock(quantityStock);
            product.setRestockLevel(restockLevel);
            product.setExpiryDate(expiryDate);
            product.setProductStatus(productStatus);

            try {
                // Update product in the database
                boolean updated = productDAO.updateProduct(product);

                // Redirect or show message based on update result
                if (updated) {
                    response.sendRedirect("success.jsp");  // Redirect to success page
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update product");
                }
            } catch (SQLException e) {
                // Handle SQL exception
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product category");
        }
    }
}
