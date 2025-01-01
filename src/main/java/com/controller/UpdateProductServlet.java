package com.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.manager.DBConnection;
import com.model.Food;
import com.model.Drink;
import com.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UpdateProductServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int prodId = Integer.parseInt(request.getParameter("prodId"));
            Connection conn = DBConnection.getConnection();
            String query = "SELECT * FROM Products WHERE PROD_ID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, prodId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product product = new Product();
                product.setProdId(prodId);
                product.setProdName(rs.getString("PROD_NAME"));
                product.setProdPrice(rs.getDouble("PROD_PRICE"));
                product.setQuantityStock(rs.getInt("QUANTITY_STOCK"));
                product.setProdStatus(rs.getString("PROD_STATUS"));
                product.setImagePath(rs.getString("IMAGE_URL")); // Fetch the image URL
                request.setAttribute("product", product);
                request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
            } else {
                response.sendRedirect("ViewProductServlet");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ViewProductServlet");
        }
    }

    // Handle POST request when the form is submitted
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int prodId = Integer.parseInt(request.getParameter("prodId"));
            String prodName = request.getParameter("prodName");
            double prodPrice = Double.parseDouble(request.getParameter("prodPrice"));
            int quantityStock = Integer.parseInt(request.getParameter("quantityStock"));
            String prodStatus = request.getParameter("prodStatus");
            String imageUrl = null;  // Initialize the imageUrl as null

            // Retrieve the image part from the form
            Part imagePart = request.getPart("image");  // This gets the file part from the form
            
            if (imagePart != null && imagePart.getSize() > 0) {
                // If the user uploaded a new image, upload it to Azure Blob Storage
                imageUrl = uploadImageToBlobStorage(imagePart);
            } else {
                // If no new image is uploaded, keep the existing image URL from the database
                imageUrl = request.getParameter("existingImageUrl");
            }

            // Update the product details in the database, including the image URL
            Connection conn = DBConnection.getConnection();
            String updateSQL = "UPDATE Products SET PROD_NAME = ?, PROD_PRICE = ?, QUANTITY_STOCK = ?, PROD_STATUS = ?, IMAGE_URL = ? WHERE PROD_ID = ?";
            PreparedStatement ps = conn.prepareStatement(updateSQL);
            ps.setString(1, prodName);
            ps.setDouble(2, prodPrice);
            ps.setInt(3, quantityStock);
            ps.setString(4, prodStatus);
            ps.setString(5, imageUrl);  // Save the image URL in the database
            ps.setInt(6, prodId);
            ps.executeUpdate();

            // Redirect to the product view page after successful update
            response.sendRedirect("ViewProductServlet");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ViewProductServlet");
        }
    }

    // This method uploads the image to Azure Blob Storage
    private String uploadImageToBlobStorage(Part imagePart) throws Exception {
        // Retrieve the connection string for Azure Blob Storage from the environment variable
        String blobConnectionString = System.getenv("BLOB_CONNECTION_STRING");

        // Create a BlobServiceClient to interact with Azure Blob Storage
        BlobContainerClient containerClient = new BlobContainerClientBuilder()
            .connectionString(blobConnectionString)
            .containerName("product-images")  // Name of your container in Azure Blob Storage
            .buildClient();

        // Generate a unique image name using the current timestamp
        String imageName = "product_" + System.currentTimeMillis() + ".jpg";
        
        // Create a BlobClient to interact with the blob for uploading
        BlobClient blobClient = containerClient.getBlobClient(imageName);

        // Upload the image file from the form to Azure Blob Storage
        InputStream imageInputStream = imagePart.getInputStream();  // Get the image input stream
        blobClient.upload(imageInputStream, imagePart.getSize(), true);  // Upload image to Blob Storage

        // Return the URL of the uploaded image
        return blobClient.getBlobUrl();
    }
}