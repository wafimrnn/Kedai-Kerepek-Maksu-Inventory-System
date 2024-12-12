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

        .sidebar h2 {
            margin: 0;
            font-size: 1.5em;
        }

        .sidebar ul {
            list-style-type: none;
            padding: 0;
            margin-top: 20px;
        }

        .sidebar ul li {
            margin: 10px 0;
        }

        .sidebar ul li a {
            text-decoration: none;
            color: white;
            font-size: 1em;
            display: block;
            padding: 10px;
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
            font-size: 2em;
        }

        .product-details p {
            font-size: 1.2em;
        }

        .back-button {
            padding: 10px 20px;
            background-color: #ffc107;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .back-button:hover {
            background-color: #e0a800;
        }
        #overlay {
    		position: fixed;
    		top: 0;
    		left: 0;
    		width: 100%;
    		height: 100%;
    		background: rgba(0, 0, 0, 0.5);
    		z-index: 1000;
		}

		#popup {
    		position: fixed;
    		top: 50%;
    		left: 50%;
		    transform: translate(-50%, -50%);
		    background: white;
		    padding: 20px;
		    border-radius: 10px;
		    text-align: center;
		    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
		    z-index: 1001;
		}

		#popup button {
		    margin: 10px;
		    padding: 10px 20px;
		    border: none;
		    border-radius: 5px;
		    cursor: pointer;
		}

		#confirmDelete {
		    background-color: #e74c3c;
		    color: white;
		}

		#cancelPopup {
		    background-color: #95a5a6;
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
                // Get product ID from the request
                String productIdStr = request.getParameter("id");
                int productId = Integer.parseInt(productIdStr);
                
                // Instantiate ProductDAO and fetch the product
                ProductDAO productDAO = new ProductDAO();
                Product product = productDAO.getProductById(productId);

                if (product != null) {
            %>
                <img src="<%= product.getImagePath() %>" alt="<%= product.getProductName() %>">
                <h2><%= product.getProductName() %></h2>
                <p><strong>Price:</strong> <%= product.getPrice() %> RM</p>
                <p><strong>Category:</strong> <%= product.getCategory() %></p> <!-- Display category -->
                <p><strong>Description:</strong> <%= product.getDescription() %></p>
                <p><strong>Stock Quantity:</strong> <%= product.getQuantity() %></p> <!-- Display quantity -->
                <p><strong>Expiry Date:</strong> <%= product.getExpiryDate() %></p>
                <button class="back-button" onclick="window.history.back()">Back to Inventory</button>
                <button class="delete-button" data-id="<%= product.getId() %>" onclick="openPopup(this)"> Delete Product </button>
                
            <% 
                } else {
            %>
                <p>Product not found.</p>
            <% 
                }
            %>
        </div>
    </div>
    <!-- Overlay -->
	<div id="overlay" style="display: none;"></div>

    <!-- Pop-Up -->
	<div id="popup" style="display: none;">
    	<h2>Delete Product Permanently?</h2>
    	<p>Are you sure you want to delete this product? This action cannot be undone.</p>
    	<button id="confirmDelete">Delete</button>
    	<button id="cancelPopup">Cancel</button>
	</div>

	<script>
        function openPopup(button) {
            const productId = button.getAttribute('data-id'); // Get product ID

            // Store the product ID in the confirmDelete button's dataset
            const confirmDelete = document.getElementById('confirmDelete');
            confirmDelete.setAttribute('data-id', productId);

            // Show the pop-up
            document.getElementById('popup').style.display = 'block';
            document.getElementById('overlay').style.display = 'block';
        }

        document.getElementById('confirmDelete').addEventListener('click', () => {
            const confirmDelete = document.getElementById('confirmDelete');
            const productId = confirmDelete.getAttribute('data-id');

            // Redirect to the delete servlet with the product ID
            window.location.href = `DeleteProductServlet?id=${productId}`;
        });

        document.getElementById('cancelPopup').addEventListener('click', () => {
            // Hide the pop-up
            document.getElementById('popup').style.display = 'none';
            document.getElementById('overlay').style.display = 'none';
        });

        document.getElementById('overlay').addEventListener('click', () => {
            // Hide the pop-up
            document.getElementById('popup').style.display = 'none';
            document.getElementById('overlay').style.display = 'none';
        });
	</script>
</body>
</html>