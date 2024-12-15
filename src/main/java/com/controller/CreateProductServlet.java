package com.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.UUID;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

public class CreateProductServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {
            try {
                // Retrieve form fields
                String productName = request.getParameter("productName");
                String category = request.getParameter("category");
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                double price = Double.parseDouble(request.getParameter("price"));
                String expiryDate = request.getParameter("expiryDate");

                // Handle category-specific fields
                Double weight = null;
                String packagingType = null;
                Integer volume = null;

                if ("FOOD".equalsIgnoreCase(category)) {
                    weight = Double.parseDouble(request.getParameter("weight"));
                    packagingType = request.getParameter("packagingType");
                } else if ("DRINK".equalsIgnoreCase(category)) {
                    volume = Integer.parseInt(request.getParameter("volume"));
                }

                // Handle file upload
                Part imagePart = request.getPart("image");
                String fileName = UUID.randomUUID() + "_" + imagePart.getSubmittedFileName();
                String imagePath = uploadToAzureBlob(fileName, imagePart.getInputStream(), imagePart.getSize());

                // Insert into database
                insertProduct(productName, category, quantity, price, expiryDate, weight, packagingType, volume, imagePath);

                // Redirect to success page
                response.sendRedirect("ViewProduct.jsp");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create product.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Form is not multipart.");
        }
    }

    // This function uploads the image to Azure Blob Storage
    private String uploadToAzureBlob(String fileName, InputStream fileInputStream, long fileSize) throws IOException {
        String connectionString = System.getenv("BLOB_CONNECTION_STRING"); // Get the connection string from environment variable
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("product-images"); // Assuming you have this container
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        blobClient.upload(fileInputStream, fileSize, true);
        return "https://<your-storage-account-name>.blob.core.windows.net/product-images/" + fileName; // Replace with your storage URL
    }

    // This function inserts product details into the database
    private void insertProduct(String productName, String category, int quantity, double price, String expiryDate,
                               Double weight, String packagingType, Integer volume, String imagePath) throws Exception {
        String sql = "INSERT INTO products (product_name, category, quantity, price, expiry_date, weight, packaging_type, volume, image_path) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); // Using the connection from the environment variable
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, productName);
            statement.setString(2, category);
            statement.setInt(3, quantity);
            statement.setDouble(4, price);
            statement.setString(5, expiryDate);
            statement.setObject(6, weight); // Use setObject for nullable fields
            statement.setString(7, packagingType);
            statement.setObject(8, volume); // Use setObject for nullable fields
            statement.setString(9, imagePath);
            statement.executeUpdate();
        }
    }

    // This function retrieves the database connection string from environment variable
    private Connection getConnection() throws Exception {
        String connectionString = System.getenv("SQL_CONNECTION_STRING"); // Get SQL connection string from environment variable
        return DriverManager.getConnection(connectionString);
    }
}