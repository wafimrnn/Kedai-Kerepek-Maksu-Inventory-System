<%@ page import="com.model.Product" %>
<%@ page import="com.dao.ProductDAO" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Product - Kedai Kerepek Maksu</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f5f5f5;
        }

        .sidebar {
            width: 250px;
            background-color: #2c2c2c;
            color: white;
            position: fixed;
            height: 100%;
            padding: 20px;
        }

        .sidebar ul {
            list-style-type: none;
            padding: 0;
        }

        .sidebar ul li a {
            text-decoration: none;
            color: white;
            padding: 10px;
            display: block;
            border-radius: 5px;
        }

        .sidebar ul li a:hover, .sidebar ul li a.active {
            background-color: #444;
        }

        .content {
            margin-left: 270px;
            padding: 20px;
        }

        .product-details {
            background-color: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        .product-details img {
            width: 100%;
            max-width: 300px;
            border-radius: 10px;
        }

        .product-details h2 {
            margin: 20px 0;
        }

        .product-details p {
            margin: 10px 0;
        }

        .button {
            padding: 10px 20px;
            margin: 10px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .back-button {
            background-color: #ffc107;
            color: white;
        }

        .delete-button {
            background-color: #e74c3c;
            color: white;
        }
    </style>
</head>
<body>
    <div class="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <ul>
            <li><a href="DashboardHome.jsp" class="active">Dashboard</a></li>
            <li><a href="#">Inventories</a></li>
            <li><a href="#">Reports</a></li>
            <li><a href="#">Profile Account</a></li>
            <li><a href="#">Log Out</a></li>
        </ul>
    </div>

    <div class="content">
        <div class="product-details">
            <% 
                String productIdStr = request.getParameter("id");
                if (productIdStr != null) {
                    try {
                        int productId = Integer.parseInt(productIdStr);
                        ProductDAO productDAO = new ProductDAO();
                        Product product = productDAO.getProductById(productId);

                        if (product != null) {
            %>
                            <img src="<%= product.getImagePath() %>" alt="<%= product.getProductName() %>">
                            <h2><%= product.getProductName() %></h2>
                            <p><strong>Price:</strong> RM <%= product.getPrice() %></p>
                            <p><strong>Category:</strong> <%= product.getCategory() %></p>
                            <p><strong>Stock Quantity:</strong> <%= product.getQuantity() %></p>
                            <p><strong>Expiry Date:</strong> <%= product.getExpiryDate() %></p>
                            <button class="button back-button" onclick="window.history.back()">Back to Inventory</button>
                            <button class="button delete-button" onclick="confirmDelete(<%= product.getProductId() %>)">Delete Product</button>
            <% 
                        } else {
            %>
                            <p>Product not found.</p>
                            <button class="button back-button" onclick="window.history.back()">Back</button>
            <% 
                        }
                    } catch (NumberFormatException e) {
            %>
                        <p>Invalid product ID.</p>
                        <button class="button back-button" onclick="window.history.back()">Back</button>
            <% 
                    }
                } else {
            %>
                    <p>No product ID provided.</p>
                    <button class="button back-button" onclick="window.history.back()">Back</button>
            <% 
                }
            %>
        </div>
    </div>

    <script>
        function confirmDelete(productId) {
            if (confirm("Are you sure you want to delete this product? This action cannot be undone.")) {
                window.location.href = `DeleteProductServlet?id=${productId}`;
            }
        }
    </script>
</body>
</html>
