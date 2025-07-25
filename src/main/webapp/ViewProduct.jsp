<%@ page import="java.util.List" %>
<%@ page import="com.model.Product" %>
<%@ page import="com.dao.ProductDAO" %>
<%@ page import="com.model.User" %> <!-- Import User model for role checking -->
<%@ page session="true" %> <!-- Allow session to access user info -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Products</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="css/viewProduct.css">
    <link rel="stylesheet" type="text/css" href="css/notification.css">
    <style>
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
            background-color: #481D01; /* Semi-transparent background color */
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
            background-color: #FEECC3;
            color: black;
        }

        .nav-links a.active {
            background-color: #F6C324; /* Background color for active link */
            color: black; /* Font color for active link */
        }
        
        
        /* Head Bar Styling */
	    	.head-bar {
		        width: calc(100% - 220px); /* Full width minus the sidebar width */
		        height: 60px;
		        background-color:  #F6C324 ;
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
	    	}
		
		    .head-bar .title {
		        font-size: 20px;
		        font-weight: bold;
		    }
		
		    .head-bar .icons {
		        display: flex;
		        align-items: center;
		        gap: 15px;
	    	}
		
		    .head-bar .icons i {
		        font-size: 20px;
		        cursor: pointer;
		        transition: color 0.3s ease;
		    }
		
		    .head-bar .icons i:hover {
		        color: #ddd;
		    }

        .main-content {
            flex: 1;
            padding: 20px;
            margin-top: 60px; /* Push content below the head bar */
        }
        
        .blurred-box {
            position: relative;
            z-index: 1;
            padding: 40px;
            background: #FBE39D;
            backdrop-filter: blur(8px); /* Ensure this is applied correctly */
            border-radius: 10px;
            margin-top: 20px; /* Set a height to center vertically */
            width: 80%;
            text-align: center;
            
        }
        
         .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .header h1 {
            font-size: 28px;
            color: #343a40;
        }

        .add-btn {
            background-color: #28a745;
            color: white;
            padding: 10px 15px;
            text-decoration: none;
            border-radius: 5px;
            font-size: 14px;
        }

        .add-btn:hover {
            background-color: #218838;
        }
        
       .product-catalog {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); /* Responsive columns */
        gap: 20px; /* Space between cards */
        margin-top: 20px;
    }

    .product-card {
	    background: white;
	    padding: 15px;
	    border-radius: 8px;
	    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
	    text-align: center;
	    width: 100%;
	    max-width: 300px; /* Ensures uniform card size */
	    min-height: 270px; /* Ensures consistency */
	    display: flex;
	    flex-direction: column;
	    justify-content: space-between; /* Distributes content evenly */
	}
	
	.product-card img {
	    width: 100%;
	    height: 150px;
	    object-fit: cover;
	    border-radius: 5px;
	    margin-bottom: 10px;
	}
	
	/* Add margin between active and inactive products */
	.inactive-products-section {
	    margin-top: 40px; /* Increased spacing */
	}
	
	.inactive-products-section h2 {
	    margin-bottom: 20px; /* Space before inactive cards */
	}

    .product-card h3 {
        font-size: 18px;
        margin-bottom: 10px;
        color: #333;
    }

    .product-card p {
        margin: 5px 0;
        font-size: 14px;
    }

        
       .button-group {
            margin-top: 15px;
        }

        .update-btn, .delete-btn {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-right: 10px;
        }

        .update-btn:hover, .delete-btn:hover {
            background-color: #45a049;
        }
        
        .update-status-btn {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-right: 10px;
        }
        
        .update-status-btn:hover {
            background-color: #45a049;
        }

        .delete-btn {
            background-color: #f44336;
            margin-right: 10px;
        }

        .delete-btn:hover {
            background-color: #d32f2f;
        }
        
        .modal {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            z-index: 1000;
            width: 300px;
            text-align: center;
        }

        .modal-content {
            padding: 20px;
        }

        .close {
            cursor: pointer;
            font-size: 20px;
            color: red;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <div class="nav-links">
            <a href="DashboardHome.jsp">Dashboard</a>
            <a href="ViewProductServlet" class="nav-link active">Product</a>
            <a href="CreateSales.jsp">Sales</a>
            <a href="Report.jsp">Report</a>
            <a href="ViewAccountServlet">Account</a>
        </div>
    </div>

    <!-- Head Bar -->
    <div class="head-bar">
        <div class="title">Products</div>
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
			                String imagePath = product.getImagePath();
			                if (imagePath == null || imagePath.isEmpty()) {
			                    imagePath = "img/default-image.jpg";
			                }
			                
			                // Show only active products in the main catalog
			                if (!"INACTIVE".equalsIgnoreCase(product.getProdStatus())) { 
			    %>
			                    <div class="product-card">
			                        <img src="<%= imagePath %>" alt="<%= product.getProdName() %>">
			                        <h3><%= product.getProdName() %></h3>
			                        <p>Price: RM <%= product.getProdPrice() %></p>
			                        <p>Stock: <%= product.getQuantityStock() %></p>
			                        <div class="button-group">
			                            <button class="update-btn" onclick="location.href='UpdateProductServlet?prodId=<%= product.getProdId() %>'">
			                                Update
			                            </button>
			                            <button class="delete-btn" onclick="confirmDelete('<%= product.getProdId() %>')">
			                                Delete
			                            </button>
			                        </div>
			                    </div>
			    <% 
			                }
			            }
			        } else { 
			    %>
			        <p>No products available.</p>
			    <% 
			        } 
			    %>
			</div>
			
			<!-- Owner-Specific Section for Inactive Products -->
			<% 
			    String userRole = (String) session.getAttribute("userRole");
			    if ("OWNER".equals(userRole)) { 
			%>
			    <div class="inactive-products-section">
    <h2>Inactive Products</h2>
    <% 
        boolean hasInactiveProducts = false;
        if (products != null) {
    %>
        <div class="product-catalog"> <!-- Add this wrapper for grid layout -->
		        <% 
		            for (Product product : products) {
		                if ("INACTIVE".equalsIgnoreCase(product.getProdStatus())) { 
		                    hasInactiveProducts = true;
		        %>
		            <div class="product-card inactive-product">
		                <img src="<%= product.getImagePath() != null ? product.getImagePath() : "img/default-image.jpg" %>" alt="<%= product.getProdName() %>">
		                <h3><%= product.getProdName() %></h3>
		                <p>Price: RM <%= product.getProdPrice() %></p>
		                <p>Stock: <%= product.getQuantityStock() %></p>
		                <p>Status: Inactive</p>
		                <div class="button-group">
		                    <button class="update-status-btn" onclick="location.href='UpdateProductStatusServlet?prodId=<%= product.getProdId() %>'">
		                        Update Status
		                    </button>
		                </div>
		            </div>
		        <% 
		                }
		            } 
		        %>
		        </div> <!-- End of product-catalog -->
		    <% 
		        } 
		        if (!hasInactiveProducts) { 
		    %>
		        <p>No inactive products available.</p>
		    <% 
		        } 
		    %>
		</div>
			<% 
			    } 
			%>
        </div>
    </div>
    <script>
        function confirmDelete(prodId) {
            if (confirm("Are you sure you want to delete this product? This action cannot be undone.")) {
                location.href = 'DeleteProductServlet?prodId=' + prodId;
            }
        }
    </script>
</body>
</html>