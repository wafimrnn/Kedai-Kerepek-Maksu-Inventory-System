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
            // Retrieve the product list from the request
            List<com.model.Product> products = (List<com.model.Product>) request.getAttribute("products");

            if (products != null && !products.isEmpty()) {
                for (com.model.Product product : products) {
        %>
            <div class="product-card">
                <img src="<%= product.getImagePath() %>" alt="<%= product.getProdName() %>" onerror="this.src='default-image.jpg'">
                <h2><%= product.getProdName() %></h2>
                <p>Price: RM <%= product.getProdPrice() %></p>
                <p>Stock: <%= product.getQuantityStock() %></p>
                <%
                    if (product instanceof com.model.Food) {
                        com.model.Food food = (com.model.Food) product;
                %>
                        <p>Category: Food</p>
                        <p>Packaging: <%= food.getPackagingType() %></p>
                        <p>Weight: <%= food.getWeight() %> kg</p>
                <%
                    } else if (product instanceof com.model.Drink) {
                        com.model.Drink drink = (com.model.Drink) product;
                %>
                        <p>Category: Drink</p>
                        <p>Volume: <%= drink.getVolume() %> L</p>
                <%
                    }
                %>
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