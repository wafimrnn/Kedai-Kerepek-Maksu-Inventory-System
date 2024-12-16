<%@ page import="java.util.List" %>
<%@ page import="com.model.Product" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Products</title>
    <style>
        /* General Styling */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }

        body {
            display: flex;
            min-height: 100vh;
            background-color: #f4f4f9;
        }

        /* Sidebar Styling */
        .sidebar {
            width: 220px;
            background-color: #343a40;
            color: white;
            display: flex;
            flex-direction: column;
            padding: 20px;
        }

        .sidebar h2 {
            margin-bottom: 20px;
            text-align: center;
            font-size: 20px;
            border-bottom: 1px solid #495057;
            padding-bottom: 10px;
        }

        .nav-links {
            display: flex;
            flex-direction: column;
        }

        .nav-links a {
            text-decoration: none;
            color: white;
            padding: 10px 15px;
            margin: 5px 0;
            border-radius: 4px;
            transition: background 0.3s ease;
        }

        .nav-links a:hover {
            background-color: #495057;
        }

        .nav-links a.active {
            background-color: #007BFF;
        }

        /* Main Content */
        .main-content {
            flex: 1;
            padding: 20px;
            background-color: white;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            border-bottom: 2px solid #e0e0e0;
            padding-bottom: 10px;
        }

        .header h1 {
            font-size: 28px;
            color: #343a40;
        }

        .add-btn {
            background-color: #007BFF;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            transition: background 0.3s ease;
        }

        .add-btn:hover {
            background-color: #0056b3;
        }

        /* Product Catalog */
        .product-catalog {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
        }

        .product-card {
            border: 1px solid #ccc;
            padding: 15px;
            width: 220px;
            text-align: center;
            border-radius: 8px;
            background-color: #f9f9f9;
            box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
        }

        .product-card img {
            width: 100%;
            height: 150px;
            object-fit: cover;
            border-radius: 8px;
        }

        .product-card h3 {
            margin: 10px 0 5px;
            font-size: 18px;
            color: #333;
        }

        .product-card p {
            margin: 5px 0;
            font-size: 14px;
            color: #555;
        }

        .button-group {
            display: flex;
            justify-content: space-around;
            margin-top: 10px;
        }

        .button-group button {
            padding: 5px 10px;
            border: none;
            border-radius: 5px;
            color: white;
            cursor: pointer;
            transition: background 0.3s ease;
        }

        .button-group .update-btn {
            background-color: #28a745;
        }

        .button-group .update-btn:hover {
            background-color: #218838;
        }

        .button-group .delete-btn {
            background-color: #dc3545;
        }

        .button-group .delete-btn:hover {
            background-color: #c82333;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <div class="nav-links">
            <a href="DashboardHome.jsp">Dashboard</a>
            <a href="ViewProductServlet.java" class="nav-link active">Product</a>
            <a href="#">Sales</a>
            <a href="#">Report</a>
            <a href="#">Account</a>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <!-- Header with Title and Add Product Button -->
        <div class="header">
            <h1>Products</h1>
            <a href="CreateProduct.html" class="add-btn">Add Product</a>
        </div>

        <!-- Product Catalog -->
        <div class="product-catalog">
    <%
        List<Product> products = (List<Product>) request.getAttribute("products");
        if (products != null && !products.isEmpty()) {
            for (Product product : products) {
    %>
                <div class="product-card">
                    <%-- Display Image or Default Image --%>
                    <%
                        String imagePath = product.getImagePath();
                        if (imagePath == null || imagePath.isEmpty()) {
                            imagePath = "default-image.jpg"; // Default image path
                        }
                    %>
                    <img src="<%= imagePath %>" alt="<%= product.getProdName() %>">
                    <h3><%= product.getProdName() %></h3>
                    <p>Price: RM <%= product.getProdPrice() %></p>
                    <p>Stock: <%= product.getQuantityStock() %></p>
                    <div class="button-group">
                        <button class="update-btn" 
                                onclick="location.href='UpdateProductServlet?prodId=<%= product.getProdId() %>'">
                            Update
                        </button>
                        <button class="delete-btn" 
                                onclick="confirmDelete('<%= product.getProdId() %>')">
                            Delete
                        </button>
                    </div>
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

<script>
    function confirmDelete(prodId) {
        if (confirm("Are you sure you want to delete this product? This action cannot be undone.")) {
            location.href = 'DeleteProductServlet?prodId=' + prodId;
        }
    }
</script>
    </div>
</body>
</html>
