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
    private static final String CONTAINER_NAME = "product-images";
    private static final String CONNECTION_STRING = System.getenv("AZURE_CONNECTION_STRING");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {
            try {
                // Retrieve form fields
                String productName = request.getParameter("product-name");
                String category = request.getParameter("category");
                String productId = request.getParameter("product-id");
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                double price = Double.parseDouble(request.getParameter("price"));

                // Handle file upload
                Part imagePart = request.getPart("product-image");
                String fileName = UUID.randomUUID() + "_" + imagePart.getSubmittedFileName();
                String imagePath = uploadToAzureBlob(fileName, imagePart.getInputStream(), imagePart.getSize());

                // Insert into database
                insertProduct(productName, category, productId, quantity, price, imagePath);

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

    private String uploadToAzureBlob(String fileName, InputStream fileInputStream, long fileSize) throws IOException {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(CONNECTION_STRING)
                .buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        blobClient.upload(fileInputStream, fileSize, true);
        return "https://kerepekmaksustorage.blob.core.windows.net/" + CONTAINER_NAME + "/" + fileName;
    }

    private void insertProduct(String productName, String category, String productId, int quantity, double price, String imagePath) throws Exception {
        String dbUrl = System.getenv("DB_URL");
        String dbUsername = System.getenv("DB_USERNAME");
        String dbPassword = System.getenv("DB_PASSWORD");

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String sql = "INSERT INTO products (product_name, category, product_id, quantity, price, image_path) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, productName);
                statement.setString(2, category);
                statement.setString(3, productId);
                statement.setInt(4, quantity);
                statement.setDouble(5, price);
                statement.setString(6, imagePath);
                statement.executeUpdate();
            }
        }
    }
}
