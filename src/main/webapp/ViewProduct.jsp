<%@ page import="java.util.List" %>
<%@ page import="com.model.Product" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Products</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="css/viewProduct.css">
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <div class="nav-links">
            <a href="DashboardHome.jsp">Dashboard</a>
            <a href="ViewProduct.jsp" class="nav-link active">Product</a>
            <a href="CreateSales.jsp">Sales</a>
            <a href="Report.html">Report</a>
            <a href="ViewAccount.jsp">Account</a>
        </div>
    </div>
    <!-- Head Bar -->
	<div class="head-bar">
	    <div class="title">Products</div>
	    <div class="icons">
	        <i class="fas fa-bell" id="notification-icon" title="Notifications"></i>
	        <i class="fas fa-user-circle" title="Account"></i>
	    </div>
	</div>

    <!-- Main Content -->
    <div class="main-content">
    <div class="blurred-box">
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
                            imagePath = "img/default-image.jpg"; // Default image path
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
</div>
<div id="notification-popup" style="display: none;">
	    <ul id="notification-list"></ul>
	</div>
</body>
</html>
