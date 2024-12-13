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

        nav {
            background-color: #2c3e50;
            width: 200px;
            height: 100vh;
            padding-top: 20px;
            position: fixed;
        }

        nav ul {
            list-style-type: none;
            padding: 0;
        }

        nav ul li {
            padding: 10px;
            text-align: center;
        }

        nav ul li a {
            color: white;
            text-decoration: none;
            display: block;
            padding: 10px;
            background-color: #34495e;
            margin: 5px 0;
            border-radius: 4px;
        }

        nav ul li a:hover {
            background-color: #16a085;
        }
        
        h3 {
            color: #2c3e50;
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
            background-color: #16a085;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            margin: 10px 0;
        }

        .button:hover {
            background-color: #1abc9c;
        }
        
        .back-button {
            background-color: #ffc107;
            color: white;
        }

    </style>
</head>
<body>
    <nav>
        <ul>
            <li><a href="Dashboard.jsp">Dashboard</a></li>
            <li><a href="ViewProduct.jsp">Inventories</a></li>
            <li><a href="Reports.jsp">Reports</a></li>
            <li><a href="ProfileAccount.jsp">Profile Account</a></li>
            <li><a href="Logout.jsp">Log Out</a></li>
        </ul>
    </nav>

    <div class="content">
        <div class="product-details">
        <h3>Products</h3>

        <a href="CreateProduct.jsp" class="button">Add Product</a>
        <a href="CreateSale.jsp" class="button">Create Sale</a>
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

        <!-- Buttons to add product and create sale -->
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