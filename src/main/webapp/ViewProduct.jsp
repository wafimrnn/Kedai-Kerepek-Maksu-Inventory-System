<%@ page import="java.util.List" %>
<%@ page import="com.model.Product" %>
<%@ page import="com.model.Food" %>
<%@ page import="com.model.Drink" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Catalog</title>
    <style>
        .product-catalog { display: flex; flex-wrap: wrap; gap: 20px; }
        .product-card { border: 1px solid #ccc; padding: 10px; width: 200px; text-align: center; }
        .product-card img { width: 100%; height: auto; }
    </style>
</head>
<body>
    <h1>Product Catalog</h1>
    
    <form action="CreateProduct.html" method="get">
        <button class="add-button">Add Product</button>
    </form>
    
    <div class="product-catalog">
    <%
        List<Product> products = (List<Product>) request.getAttribute("products");
        if (products != null && !products.isEmpty()) {
            for (Product product : products) {
                String imagePath = product.getImagePath();
    %>
                <div class="product-card">
                    <h3><%= product.getProdName() %></h3>
                    <p>Price: RM <%= product.getProdPrice() %></p>
                    <p>Stock: <%= product.getQuantityStock() %></p>
                    
                    <%-- Debug image paths in JSP --%>
                    <p>Image Path: <%= imagePath != null ? imagePath : "Default Image" %></p>
                    
                    <% 
                        if (imagePath != null && !imagePath.isEmpty()) { 
                    %>
                        <img src="<%= imagePath %>" alt="<%= product.getProdName() %>">
                    <% 
                        } else { 
                    %>
                        <img src="img/default-image.jpg" alt="Default Image">
                    <% } %>
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
