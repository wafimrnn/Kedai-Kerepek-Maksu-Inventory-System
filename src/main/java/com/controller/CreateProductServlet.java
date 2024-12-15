package com.controller;

import com.azure.storage.blob.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.UUID;
import java.sql.*;

public class CreateProductServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Retrieve form parameters
        String productName = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String category = request.getParameter("category");

        // Step 2: Handle image upload (if available)
        Part filePart = request.getPart("image");
        String imageUrl = null;

        if (filePart != null) {
            imageUrl = uploadImageToBlob(filePart); // Upload the image to Blob Storage
        }

        if (imageUrl != null) {
            // Step 3: Save the product to the database, including image URL
            saveProductToDatabase(productName, price, quantity, category, imageUrl);
            response.sendRedirect("product-list.jsp"); // Redirect after successful creation
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Image file is required.");
        }
    }

    // Step 4: Upload the image to Azure Blob Storage and return the image URL
    private String uploadImageToBlob(Part filePart) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + filePart.getSubmittedFileName();
        
        // Use the Blob connection string provided by Azure Service Connector
        String blobConnectionString = System.getenv("BLOB_CONNECTION_STRING");
        
        // Initialize BlobServiceClient using the connection string
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
            .connectionString(blobConnectionString)
            .buildClient();

        // Define your container name (ensure the container exists in Blob Storage)
        String containerName = "your-container-name"; // Replace with your actual container name
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        // Create a BlobClient to represent the new image file in the container
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        // Upload the file to Blob Storage
        try (InputStream inputStream = filePart.getInputStream()) {
            blobClient.upload(inputStream, filePart.getSize(), true);  // Overwrite if the blob already exists
        }

        // Return the URL of the uploaded image
        return blobClient.getBlobUrl().toString();
    }

    // Step 5: Save product details to the database (including image URL)
    private void saveProductToDatabase(String productName, double price, int quantity, String category, String imageUrl) {
        // SQL query to insert product data
        String sql = "INSERT INTO Products (PROD_NAME, PROD_PRICE, QUANTITY_STOCK, PROD_STATUS, IMAGE_PATH) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); // Database connection
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            // Set parameters for the SQL query
            ps.setString(1, productName);
            ps.setDouble(2, price);
            ps.setInt(3, quantity);
            ps.setString(4, "Active");  // Default product status
            ps.setString(5, imageUrl);  // Save image URL to the database

            // Execute the insert statement
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();  // Log the exception
        }
    }

    // Utility method to establish a connection to the database (adjust according to your DB setup)
    private Connection getConnection() throws SQLException {
        String dbUrl = System.getenv("SQL_CONNECTION_STRING");  // Use the environment variable for SQL connection
        try {
            return DriverManager.getConnection(dbUrl);  // Establish the connection
        } catch (SQLException e) {
            throw new SQLException("Error connecting to the database", e);
        }
    }
}
