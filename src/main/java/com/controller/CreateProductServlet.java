package com.controller;

import com.azure.storage.blob.*;
import com.manager.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class CreateProductServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Starting CreateProductServlet...");

        // Step 1: Retrieve form data
        String prodName = request.getParameter("prodName");
        String prodPrice = request.getParameter("prodPrice");
        String quantityStock = request.getParameter("quantityStock");
        Part filePart = request.getPart("image");  // Retrieve the uploaded image file
        System.out.println("Parameters received: prodName=" + prodName + ", prodPrice=" + prodPrice + ", quantityStock=" + quantityStock);

        if (prodName == null || prodPrice == null || quantityStock == null || filePart == null) {
            System.out.println("Missing form data.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing form data.");
            return;
        }

        // Step 2: Upload image to Blob Storage
        String imageUrl = uploadToBlob(filePart);
        if (imageUrl == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Blob upload failed.");
            return;
        }
        System.out.println("Image successfully uploaded to Blob Storage: " + imageUrl);

        // Step 3: Insert product into SQL database
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Products (prodName, prodPrice, quantityStock, imagePath) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, prodName);
                ps.setDouble(2, Double.parseDouble(prodPrice));
                ps.setInt(3, Integer.parseInt(quantityStock));
                ps.setString(4, imageUrl);

                int rows = ps.executeUpdate();
                System.out.println("Rows inserted: " + rows);

                if (rows > 0) {
                    response.sendRedirect("success.html");  // Redirect to success page
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to insert product.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }

    private String uploadToBlob(Part filePart) {
        String blobConnectionString = System.getenv("AZURE_STORAGEBLOB_CONNECTIONSTRING");
        String containerName = "product-images";  // Replace with your actual container name
        String fileName = UUID.randomUUID().toString() + "-" + filePart.getSubmittedFileName();

        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(blobConnectionString)
                .buildClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = containerClient.getBlobClient(fileName);

            try (InputStream inputStream = filePart.getInputStream()) {
                blobClient.upload(inputStream, filePart.getSize(), true);
            }
            return blobClient.getBlobUrl();  // Return the URL of the uploaded image
        } catch (Exception e) {
            System.err.println("Blob upload failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
