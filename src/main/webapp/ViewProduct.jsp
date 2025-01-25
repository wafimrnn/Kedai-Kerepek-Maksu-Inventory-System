<%@ page import="java.util.List" %>
<%@ page import="com.model.Product" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Products</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
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
            background-image: url('img/pisangImage.jpg'); /* Background image */
            background-size: cover; /* Cover the entire area */
            background-position: center; /* Center the image */
            position: relative;
        }

        /* Sidebar Styling */
        .sidebar {
            width: 220px;
            background-color: rgba(241, 241, 109, 0.62); /* Semi-transparent background color */
            color: white;
            display: flex;
            flex-direction: column;
            padding: 20px;
            backdrop-filter: blur(10px); /* Apply blur effect to the background */
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
            color: white; /* Default font color */
            padding: 10px 15px;
            margin: 5px 0;
            border-radius: 4px;
            transition: background 0.3s ease;
        }

        .nav-links a:hover {
            background-color: #495057;
        }

        .nav-links a.active {
            background-color: #f1ea82; /* Background color for active link */
            color: #5c5c52; /* Font color for active link */
        }
        
        /* Head Bar Styling */
        .head-bar {
            width: calc(100% - 220px); /* Full width minus the sidebar width */
            height: 60px;
            background-color: rgba(227, 227, 80, 0.62); /* Semi-transparent background color */
            color: white;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0 20px;
            position: fixed;
            top: 0;
            left: 220px; /* Push the head bar right to align with the sidebar */
            z-index: 1000; /* Ensure it stays on top */
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            backdrop-filter: blur(10px); /* Apply blur effect to the background */
        }
        
        .head-bar .title {
            font-size: 20px;
            font-weight: bold;
            color: #ffffff;
        }
        
        .head-bar .icons {
            display: flex;
            align-items: center;
            gap: 15px;
            color: #ffffff;
        }
        
        .head-bar .icons i {
            font-size: 20px;
            cursor: pointer;
            transition: color 0.3s ease;
        }
        
        .head-bar .icons i:hover {
            color: #ddd;
        }

        /* Main Content */
        .main-content {
            flex: 1;
            padding: 20px;
            position: relative; /* Position relative for the overlay */
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: flex-start;
            margin-top: 60px; /* Push content below the head bar */
            overflow: hidden; /* Prevent overflow */
        }
        
        .main-content::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-image: url('img/pisangImage.jpg'); /* Path to your image */
            background-size: cover; /* Cover the entire area */
            background-position: center; /* Center the image */
            background-repeat: no-repeat; /* Prevent the image from repeating */
            
            filter: blur(1px); /* Adjust blur intensity */
            z-index: -1; /* Push background below all content */
        }
        
        .main-content h1 {
      		font-size: 60px;
      		font-weight: bold;
      		margin-bottom: 90px;
      		position: relative;
      		z-index: 2;
      		color: #545445;
    	}
        
       
        
        .blurred-box {
    		position: relative;
    		z-index: 1;
    		padding: 40px;
    		background: rgba(255, 250, 171, 0.62); /* Light yellow semi-transparent background */
    		backdrop-filter: blur(8px); /* Blurred effect */
    		border-radius: 10px;
    		margin: 20px auto; /* Center the box and add spacing */
    		display: flex; /* Flexbox container */
    		flex-direction: column; /* Stack items vertically */
    		justify-content: center; /* Center vertically */
    		box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Optional shadow for depth */
    		width: calc(100% - 60px); /* Adjust width to fit within padding */
    		max-width: 1200px; /* Ensure it doesn't stretch too much */
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
	        <i class="fas fa-bell" title="Notifications"></i>
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
</body>
</html>
