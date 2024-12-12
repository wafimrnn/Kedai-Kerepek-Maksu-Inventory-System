package com.controller;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import jakarta.servlet.ServletException;
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

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

@WebServlet("/CreateProductServlet")
public class CreateProductServlet extends HttpServlet {
    private static final String CONTAINER_NAME = "product-images"; // The name of your Blob container
    private static final String CONNECTION_STRING = System.getenv("AZURE_CONNECTION_STRING"); // Use environment variable for connection string

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (ServletFileUpload.isMultipartContent(request)) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            String productName = "", category = "", productId = "", imagePath = "";
            int quantity = 0;
            double price = 0.0;

            try {
                // Corrected: parsing the request
                List<FileItem> formItems = upload.parseRequest((RequestContext) request);

                for (FileItem item : formItems) {
                    if (item.isFormField()) {
                        // Process form fields (product details)
                        String fieldName = item.getFieldName();
                        if ("product-name".equals(fieldName)) {
                            productName = item.getString();
                        } else if ("category".equals(fieldName)) {
                            category = item.getString();
                        } else if ("product-id".equals(fieldName)) {
                            productId = item.getString();
                        } else if ("quantity".equals(fieldName)) {
                            quantity = Integer.parseInt(item.getString());
                        } else if ("price".equals(fieldName)) {
                            price = Double.parseDouble(item.getString());
                        }
                    } else {
                        // Process the uploaded file (image)
                        String fieldName = item.getFieldName();
                        if ("product-image".equals(fieldName)) {
                            // Get file name
                            String fileName = new File(item.getName()).getName();
                            InputStream fileInputStream = item.getInputStream();

                            // Upload the image to Azure Blob Storage
                            uploadToAzureBlob(fileName, fileInputStream);

                            // Set the image path (URL to the uploaded image)
                            imagePath = "https://kerepekmaksustorage.blob.core.windows.net/" + CONTAINER_NAME + "/" + fileName;
                        }
                    }
                }

                // Save product information with image path in the database
                try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://maksukerepek.database.windows.net:1433;database=KedaiMaksuDB;", "maksuadmin", "Larvapass@")) {
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

                // Redirect to product view page
                response.sendRedirect("ViewProduct.jsp");

            } catch (Exception ex) {
                ex.printStackTrace();
                response.getWriter().write("Error: " + ex.getMessage());
            }
        } else {
            response.getWriter().write("Error: Form is not multipart.");
        }
    }

    // This is the method that handles uploading the image to Azure Blob Storage
    private void uploadToAzureBlob(String fileName, InputStream fileInputStream) throws IOException {
        // Create a BlobServiceClient object using the connection string
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(CONNECTION_STRING)
                .buildClient();

        // Get a reference to the container where the file will be uploaded
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);

        // Get a reference to the blob (file) to be uploaded
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        // Upload the file
        blobClient.upload(fileInputStream, fileInputStream.available(), true); // true to overwrite if file exists
    }
}