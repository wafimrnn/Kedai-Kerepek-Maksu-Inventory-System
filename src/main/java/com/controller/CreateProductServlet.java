package com.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        try {
            // Retrieve form data
            String productName = request.getParameter("productName");
            int quantityStock = Integer.parseInt(request.getParameter("quantity"));
            double price = Double.parseDouble(request.getParameter("price"));
            Date expiryDate = Date.valueOf(request.getParameter("expiryDate"));
            int restockLevel = 10; // Default value or retrieve from form if needed
            String productStatus = "Active"; // Default status or retrieve from form if needed
            String imagePath = ""; // Handle image upload as needed, e.g., using Azure Blob Storage

            // Retrieve the product type (FOOD or DRINK)
            String productType = request.getParameter("category");

            Product product = null;

            if ("FOOD".equalsIgnoreCase(productType)) {
                // Retrieve food-specific fields
                double weight = Double.parseDouble(request.getParameter("weight"));
                String packagingType = request.getParameter("packagingType");

                // Create Food object using the constructor
                product = new Food(0, productName, quantityStock, price, expiryDate, restockLevel, productStatus, imagePath, packagingType, weight);
            } else if ("DRINK".equalsIgnoreCase(productType)) {
                // Retrieve drink-specific fields
                int volume = Integer.parseInt(request.getParameter("volume"));

                // Create Drink object using the constructor
                product = new Drink(0, productName, quantityStock, price, expiryDate, restockLevel, productStatus, imagePath, volume);
            }

            // Save the product using DAO
            boolean isProductAdded = productDAO.addProduct(product);
            if (isProductAdded) {
                response.sendRedirect("ViewProduct.jsp");
            } else {
                request.setAttribute("errorMessage", "Failed to add the product.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("errorPage.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("errorPage.jsp");
            dispatcher.forward(request, response);
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