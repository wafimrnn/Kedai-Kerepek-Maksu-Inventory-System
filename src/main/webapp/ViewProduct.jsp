<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.model.Product" %>
<%@ page import="java.util.List" %>

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
                for (Product product : products) {
        %>
            <div class="product-card">
                <img src="<%= product.getImagePath() %>" alt="<%= product.getProductName() %>" class="product-image">
                <h2><%= product.getProductName() %></h2>
                <p>Price: $<%= product.getPrice() %></p>
                <p>Stock Quantity: <%= product.getQuantityStock() %></p>
                <p>Expiry Date: <%= product.getExpiryDate() %></p>
                <!-- Add more attributes as necessary -->
                <button>Add to Cart</button>
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