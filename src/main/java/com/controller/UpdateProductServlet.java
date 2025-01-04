package com.controller;

import com.manager.DBConnection;
import com.manager.BlobStorage;
import com.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@MultipartConfig
public class UpdateProductServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String prodIdParam = request.getParameter("prodId");
        if (prodIdParam == null || prodIdParam.isEmpty()) {
            response.sendRedirect("ViewProductServlet");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            int prodId = Integer.parseInt(prodIdParam);
            String query = "SELECT * FROM Products WHERE PROD_ID = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, prodId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Product product = new Product();
                        product.setProdId(prodId);
                        product.setProdName(rs.getString("PROD_NAME"));
                        product.setProdPrice(rs.getDouble("PROD_PRICE"));
                        product.setQuantityStock(rs.getInt("QUANTITY_STOCK"));
                        product.setProdStatus(rs.getString("PROD_STATUS"));
                        product.setImagePath(rs.getString("IMAGE_PATH")); // Existing image URL

                        request.setAttribute("product", product);
                        request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
                        return;
                    }
                }
            }
            response.sendRedirect("ViewProductServlet");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ViewProductServlet");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int prodId = Integer.parseInt(request.getParameter("prodId"));
            String prodName = request.getParameter("prodName");
            double prodPrice = Double.parseDouble(request.getParameter("prodPrice"));
            int quantityStock = Integer.parseInt(request.getParameter("quantityStock"));
            String prodStatus = request.getParameter("prodStatus"); // Availability status
            String category = request.getParameter("category"); // Category (Food/Drink)

            // Handle existing/new image
            Part imagePart = request.getPart("image");
            String imageUrl = request.getParameter("existingImageUrl");
            if (imagePart != null && imagePart.getSize() > 0) {
                BlobStorage blobManager = new BlobStorage();
                imageUrl = blobManager.uploadImage(imagePart, "product-images");
            }

            // Update the product in the database
            try (Connection conn = DBConnection.getConnection()) {
                String updateSQL = "UPDATE Products SET PROD_NAME = ?, PROD_PRICE = ?, QUANTITY_STOCK = ?, PROD_STATUS = ?, IMAGE_PATH = ? WHERE PROD_ID = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateSQL)) {
                    ps.setString(1, prodName);
                    ps.setDouble(2, prodPrice);
                    ps.setInt(3, quantityStock);
                    ps.setString(4, prodStatus); // Correctly update the availability status
                    ps.setString(5, imageUrl);
                    ps.setInt(6, prodId);
                    ps.executeUpdate();
                }
            }

            response.sendRedirect("ViewProductServlet");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ViewProductServlet");
        }
    }
}