package com.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.UUID;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@WebServlet("/CreateProductServlet")
public class CreateProductServlet extends HttpServlet {
    private static final String CONTAINER_NAME = "product-images"; // Azure Blob Storage container name
    private static final String CONNECTION_STRING = System.getenv("AZURE_CONNECTION_STRING"); // Azure connection string

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the form is a multipart request (i.e., contains files)
        if (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {

            // Initialize product variables
            String productName = "", category = "", productId = "", imagePath = "";
            int quantity = 0;
            double price = 0.0;

            try {
                // Get parts of the request
                for (Part part : request.getParts()) {
                    String partName = part.getName();

                    // Handle regular form fields (non-file inputs)
                    if (partName.equals("product-name")) {
                        productName = getFormFieldValue(part);
                    } else if (partName.equals("category")) {
                        category = getFormFieldValue(part);
                    } else if (partName.equals("product-id")) {
                        productId = getFormFieldValue(part);
                    } else if (partName.equals("quantity")) {
                        quantity = Integer.parseInt(getFormFieldValue(part));
                    } else if (partName.equals("price")) {
                        price = Double.parseDouble(getFormFieldValue(part));
                    } else if (partName.equals("product-image")) {
                        // Handle file upload (product image)
                        String fileName = UUID.randomUUID() + "_" + new File(part.getSubmittedFileName()).getName();
                        try (InputStream fileInputStream = part.getInputStream()) {
                            // Upload to Azure Blob Storage
                            uploadToAzureBlob(fileName, fileInputStream, part.getSize());
                            imagePath = "https://kerepekmaksustorage.blob.core.windows.net/" + CONTAINER_NAME + "/" + fileName;
                        }
                    }
                }

                // Database connection details
                String dbUrl = System.getenv("DB_URL");
                String dbUsername = System.getenv("DB_USERNAME");
                String dbPassword = System.getenv("DB_PASSWORD");

                // Insert product details into the database
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

                // Redirect to the product view page after successful insertion
                response.sendRedirect("ViewProduct.jsp");

            } catch (Exception ex) {
                // Handle exceptions and log the error
                ex.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create product.");
            }
        } else {
            // If the form is not multipart, send a bad request response
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Form is not multipart.");
        }
    }

    private String getFormFieldValue(Part part) throws IOException {
        // Read the form field value (non-file input)
        try (InputStream inputStream = part.getInputStream()) {
            return new String(inputStream.readAllBytes());
        }
    }

    private void uploadToAzureBlob(String fileName, InputStream fileInputStream, long fileSize) throws IOException {
        // Initialize Azure Blob Service Client using the connection string
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(CONNECTION_STRING)
                .buildClient();

        // Get the Blob Container Client
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);

        // Get the Blob Client for the specific file
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        // Upload the file to Azure Blob Storage (overwriting if it already exists)
        blobClient.upload(fileInputStream, fileSize, true);
    }
}