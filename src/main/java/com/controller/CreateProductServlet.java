package com.controller;

import com.azure.storage.blob.*;
import com.manager.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
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

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1 MB
    maxFileSize = 5 * 1024 * 1024,  // 5 MB
    maxRequestSize = 10 * 1024 * 1024 // 10 MB
)
public class CreateProductServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Starting CreateProductServlet...");

        // Debug: Log all received parameters
        System.out.println("Debugging received parameters:");
        request.getParameterMap().forEach((key, value) ->
            System.out.println(key + " = " + String.join(",", value))
        );

        // Step 1: Retrieve form data
        String prodName = request.getParameter("prodName");
        String prodPrice = request.getParameter("prodPrice");
        String quantityStock = request.getParameter("quantityStock");
        String restockLevel = request.getParameter("restockLevel");
        String expiryDate = request.getParameter("expiryDate");
        String category = request.getParameter("category");
        Part filePart = request.getPart("image");

        // Specific attributes for Food or Drink
        String packagingType = request.getParameter("packagingType");
        String weight = request.getParameter("weight");
        String volume = request.getParameter("volume");

        // Debug: Log extracted parameters
        System.out.println("Parsed Parameters:");
        System.out.println("prodName: " + prodName);
        System.out.println("prodPrice: " + prodPrice);
        System.out.println("quantityStock: " + quantityStock);
        System.out.println("restockLevel: " + restockLevel);
        System.out.println("expiryDate: " + expiryDate);
        System.out.println("category: " + category);
        System.out.println("packagingType: " + packagingType);
        System.out.println("weight: " + weight);
        System.out.println("volume: " + volume);
        System.out.println("File part is null? " + (filePart == null));

        // Step 2: Validate mandatory fields
        if (prodName == null || prodPrice == null || quantityStock == null || restockLevel == null || category == null || filePart == null) {
            System.out.println("Error: Missing form data.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing form data.");
            return;
        }

        // Step 3: Upload image to Blob Storage
        String imageUrl = uploadToBlob(filePart);
        if (imageUrl == null) {
            System.out.println("Blob upload failed. Returning 500 error.");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Blob upload failed.");
            return;
        }
        System.out.println("Image successfully uploaded to Blob Storage: " + imageUrl);

        // Step 4: Insert product into database
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Inserting into Products table...");
            String sqlProduct = "INSERT INTO Products (PROD_NAME, PROD_PRICE, QUANTITY_STOCK, RESTOCK_LEVEL, EXPIRY_DATE, IMAGE_PATH, PROD_STATUS) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlProduct, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, prodName);
                ps.setDouble(2, Double.parseDouble(prodPrice));
                ps.setInt(3, Integer.parseInt(quantityStock));
                ps.setInt(4, Integer.parseInt(restockLevel));
                ps.setString(5, expiryDate != null ? expiryDate : null);
                ps.setString(6, imageUrl);
                ps.setString(7, "ACTIVE");

                int rows = ps.executeUpdate();
                System.out.println("Rows inserted into Products table: " + rows);

                if (rows > 0) {
                    try (var rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            int prodId = rs.getInt(1);
                            System.out.println("Generated Product ID: " + prodId);

                            if ("Food".equalsIgnoreCase(category)) {
                                insertFood(conn, prodId, packagingType, weight);
                            } else if ("Drink".equalsIgnoreCase(category)) {
                                insertDrink(conn, prodId, volume);
                            }
                        }
                    }
                    response.sendRedirect("ViewProduct.jsp");
                } else {
                    System.out.println("Failed to insert into Products table.");
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to insert product.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }

    private String uploadToBlob(Part filePart) {
        System.out.println("Uploading image to Blob Storage...");
        String blobConnectionString = System.getenv("AZURE_STORAGEBLOB_CONNECTIONSTRING");
        String containerName = "product-images";
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
            System.out.println("Image uploaded successfully. Blob URL: " + blobClient.getBlobUrl());
            return blobClient.getBlobUrl();
        } catch (Exception e) {
            System.out.println("Blob upload failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void insertFood(Connection conn, int prodId, String packagingType, String weight) throws SQLException {
        System.out.println("Inserting into Food table...");
        if (packagingType != null && weight != null) {
            String sqlFood = "INSERT INTO Food (PROD_ID, PACKAGING_TYPE, WEIGHT) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlFood)) {
                ps.setInt(1, prodId);
                ps.setString(2, packagingType);
                ps.setDouble(3, Double.parseDouble(weight));
                ps.executeUpdate();
            }
        }
    }

    private void insertDrink(Connection conn, int prodId, String volume) throws SQLException {
        System.out.println("Inserting into Drink table...");
        if (volume != null) {
            String sqlDrink = "INSERT INTO Drink (PROD_ID, VOLUME) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlDrink)) {
                ps.setInt(1, prodId);
                ps.setDouble(2, Double.parseDouble(volume));
                ps.executeUpdate();
            }
        }
    }
}