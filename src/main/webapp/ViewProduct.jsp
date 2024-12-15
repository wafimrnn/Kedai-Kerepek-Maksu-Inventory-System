<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.model.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>View Products</title>
    <link rel="stylesheet" href="style/ViewProduct.css"> <!-- Your existing style file -->
</head>
<body>

    <!-- Sidebar -->
    <div id="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <ul>
            <li><a href="DashboardHome.jsp">Dashboard</a></li>
            <li><a href="ViewProduct.jsp">Products</a></li>
            <li><a href="#">Sales</a></li>
            <li><a href="#">Report</a></li>
            <li><a href="#">Account</a></li>
        </ul>
    </div>

    <!-- Main Content -->
    <h1>Products</h1>
        
    <!-- Add Product Button -->
    <a href="CreateProduct.html" class="button">Add Product</a>

    <div class="product-container">
        <% 
            // Retrieve the list of products from the request
            List<Product> products = (List<Product>) request.getAttribute("productList");
            
            if (products != null && !products.isEmpty()) {
                // Debugging to ensure the product list is being passed correctly
                System.out.println("Product list size: " + products.size());
                
                for (Product product : products) {
                    // Debugging product details
                    System.out.println("Product Name: " + product.getProductName());
                    System.out.println("Product Image Path: " + product.getImagePath());
        %>
            <div class="product-card">
                <!-- Image Path with a fallback to a default image if not provided -->
                <img src="<%= (product.getImagePath() != null && !product.getImagePath().isEmpty()) ? product.getImagePath() : "default.jpg" %>" 
                     alt="<%= product.getProductName() %>" class="product-image">
                <h2><%= product.getProductName() %></h2>
                <p>Price: $<%= product.getPrice() %></p>
                <p>Stock Quantity: <%= product.getQuantityStock() %></p>
                <% 
    				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    				String formattedExpiryDate = sdf.format(product.getExpiryDate());
				%>

				<p>Expiry Date: <%= formattedExpiryDate %></p>
            </div>
        <% 
                }
            } else {
        %>
            <p>No products available.</p>
        <% 
            }
        %>
    </div>

</body>
</html>