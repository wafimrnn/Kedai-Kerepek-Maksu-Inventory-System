package com.controller;

import com.azure.storage.blob.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.UUID;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet("/CreateProductServlet")
public class CreateProductServlet extends HttpServlet {
    private static final String CONTAINER_NAME = "product-images"; // Azure Blob Storage container name
    private static final String CONNECTION_STRING = System.getenv("AZURE_CONNECTION_STRING"); // Azure connection string

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the form is a multipart request (i.e., contains files)
        if (ServletFileUpload.isMultipartContent((javax.servlet.http.HttpServletRequest) request)) {
            ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());

            // Initialize product variables
            String productName = "", category = "", productId = "", imagePath = "";
            int quantity = 0;
            double price = 0.0;

            try {
                // Parse the form data
                List<org.apache.commons.fileupload.FileItem> formItems = upload.parseRequest((javax.servlet.http.HttpServletRequest) request);
                for (org.apache.commons.fileupload.FileItem item : formItems) {
                    if (((org.apache.commons.fileupload.FileItem) item).isFormField()) {
                        // Extract form fields
                        switch (item.getName()) {
                            case "product-name" -> productName = item.toString();
                            case "category" -> category = item.toString();
                            case "product-id" -> productId = item.toString();
                            case "quantity" -> quantity = Integer.parseInt(item.toString());
                            case "price" -> price = Double.parseDouble(item.toString());
                        }
                    } else if ("product-image".equals(item.getName())) {
                        // Handle file upload (product image)
                        String fileName = UUID.randomUUID() + "_" + new File(item.getName()).getName();
                        try (InputStream fileInputStream = ((ServletRequest) item).getInputStream()) {
                            // Upload to Azure Blob Storage
                            uploadToAzureBlob(fileName, fileInputStream, ((org.apache.commons.fileupload.FileItem) item).getSize());
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