package com.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.UUID;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.dao.ProductDAO;
import com.model.Drink;
import com.model.Food;
import com.model.Product;

public class CreateProductServlet extends HttpServlet {

    private ProductDAO productDAO = new ProductDAO(); // Create an instance of ProductDAO

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
                int restockLevel = Integer.parseInt(request.getParameter("restockLevel"));
                // Set default product status to 'ACTIVE'
                String productStatus = "ACTIVE"; // Default status
                // If the form has a product status value, use it
                if (request.getParameter("productStatus") != null) {
                    productStatus = request.getParameter("productStatus");
                }

                // Handle category-specific fields
                Double weight = null;
                String packagingType = null;
                Integer volume = null;

                // Handle category-specific fields
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

                // Create the appropriate Product object based on the category
                Product product = null;

                if ("FOOD".equalsIgnoreCase(category)) {
                    product = new Food(0, productName, quantity, price, Date.valueOf(expiryDate), restockLevel, imagePath, productStatus, weight, packagingType);
                } else if ("DRINK".equalsIgnoreCase(category)) {
                    product = new Drink(0, productName, quantity, price, Date.valueOf(expiryDate), restockLevel, imagePath, productStatus, volume);
                }
                
                

                // Call DAO method to add product to database
                boolean success = productDAO.addProduct(product);

                if (success) {
                    response.sendRedirect("ViewProduct.jsp");
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create product.");
                }
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
        String connectionString = System.getenv("BLOB_CONNECTION_STRING");
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("product-images");
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        blobClient.upload(fileInputStream, fileSize, true);
        return "https://<your-storage-account-name>.blob.core.windows.net/product-images/" + fileName;
    }
}