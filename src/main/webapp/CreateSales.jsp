<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dao.ProductDAO" %>
<%@ page import="com.model.Product" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>POS System</title>
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
        
        /* Head Bar Styling */
		.head-bar {
		    width: calc(100% - 220px); /* Full width minus the sidebar width */
		    height: 60px;
		    background-color: #007BFF;
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

        /* Main Content Styling */
		.main-content {
		    flex: 1;
		    margin-top: 60px; /* Leave space for the head bar */
		    display: flex;
		    flex-direction: row;
		    padding: 20px;
		    background-color: #ecf0f1;
		    height: calc(100vh - 60px); /* Subtract the height of the head bar */
		    overflow-y: auto;
		}
		
		/* Product List Styling */
		.product-list {
		    flex: 2;
		    margin-right: 20px;
		}
		
		.product-list h3 {
		    margin-bottom: 10px;
		}
		
		.product-grid {
		    display: grid;
		    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
		    gap: 10px;
		}
		
		.product-item {
		    background-color: #fff;
		    padding: 10px;
		    border-radius: 5px;
		    text-align: center;
		    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
		}
		
		.product-item img {
		    width: 100%;
		    height: 100px;
		    object-fit: cover;
		    border-radius: 5px;
		    margin-bottom: 10px;
		}
		
		.product-item button {
		    background-color: #3498db;
		    color: white;
		    border: none;
		    padding: 5px 10px;
		    border-radius: 3px;
		    cursor: pointer;
		}
		
		.product-item button:hover {
		    background-color: #2980b9;
		}
		
		/* Order Calculation Section */
		.order-calculation {
		    flex: 1;
		    background-color: #fff;
		    padding: 20px;
		    border-radius: 5px;
		    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
		    max-width: 400px;
		    height: fit-content; /* Makes sure the height fits its content */
		}
		
		/* Order Details Styling */
		.order-details table {
		    width: 100%;
		    border-collapse: collapse;
		    margin-bottom: 20px;
		}
		
		.order-details table th, .order-details table td {
		    text-align: left;
		    padding: 10px;
		    border-bottom: 1px solid #ddd;
		}
		
		.totals {
		    margin-bottom: 20px;
		    font-size: 16px;
		}
		
		.payment-method {
		    margin-bottom: 15px;
		}
		
		.payment-method button {
		    width: 48%;
		    padding: 10px;
		    margin-right: 2%;
		    background-color: #3498db;
		    color: white;
		    border: none;
		    border-radius: 5px;
		    cursor: pointer;
		}
		
		.payment-method button:hover {
		    background-color: #2980b9;
		}
		
		.payment-method button:last-child {
		    margin-right: 0;
		}
		
		.payment-details {
		    display: none;
		}
		
		.payment-details.active {
		    display: block;
		}
		
		.payment-details input {
		    width: 100%;
		    padding: 10px;
		    border: 1px solid #ddd;
		    border-radius: 5px;
		    margin-bottom: 10px;
		}
		
		.payment-details button {
		    width: 100%;
		    padding: 10px;
		    background-color: #2ecc71;
		    color: white;
		    border: none;
		    border-radius: 5px;
		    cursor: pointer;
		}
		
		.payment-details button:hover {
		    background-color: #27ae60;
		}
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <div class="nav-links">
            <a href="DashboardHome.jsp">Dashboard</a>
            <a href="ViewProductServlet">Product</a>
            <a href="CreateSales.jsp" class="active">Sales</a>
            <a href="Report.jsp">Report</a>
            <a href="#">Account</a>
        </div>
    </div>

    <!-- Head Bar -->
    <div class="head-bar">
        <div class="title">Sales</div>
        <div class="icons">
            <i class="fas fa-bell" title="Notifications"></i>
            <i class="fas fa-user-circle" title="Account"></i>
        </div>
    </div>

    <div class="main-content">
	    <!-- Product List -->
	    <div class="product-list">
	        <div class="header">
	            <h1>Create Sales</h1>
	        </div>
	        <div id="product-grid" class="product-grid">
	            <%
	                ProductDAO productDAO = new ProductDAO();
	                List<Product> productList = productDAO.getAllActiveProducts();
	                for (Product product : productList) {
	            %>
	            <div class="product-item">
	                <img src="<%= product.getImagePath() %>" alt="<%= product.getProdName() %>">
	                <h4><%= product.getProdName() %></h4>
	                <p>Price: RM <%= product.getProdPrice() %></p>
	                <button onclick="addToOrder('<%= product.getProdName() %>', <%= product.getProdPrice() %>)">Add to Order</button>
	            </div>
	            <% } %>
	        </div>
	    </div>

	    <!-- Order Calculation -->
	    <div class="order-calculation">
	        <div class="order-details">
	            <h3>Order Details</h3>
	            <table>
	                <thead>
					    <tr>
					        <th>Item</th>
					        <th>Qty</th>
					        <th>Subtotal</th>
					        <th>Action</th>
					    </tr>
					</thead>
	                <tbody id="order-items">
	                    <!-- Order items will be added dynamically here -->
	                </tbody>
	            </table>
	            <div class="totals">
	                <p>Subtotal: <span id="subtotal">RM 0</span></p>
	                <p><strong>Total: <span id="total">RM 0</span></strong></p>
	            </div>
	        </div>
	
	        <div class="payment-method">
	            <button onclick="togglePayment('cash')">Cash</button>
	            <button onclick="togglePayment('qr')">QR Payment</button>
	        </div>
	
	        <div id="cash" class="payment-details">
	            <h3>Cash Payment</h3>
	            <input type="text" id="money-received" placeholder="Money Received" oninput="calculateChange()">
	            <input type="text" id="change" placeholder="Change" disabled>
	            <button onclick="completeOrder()">Complete Order</button>
	        </div>
	
	        <div id="qr" class="payment-details">
	            <h3>QR Payment</h3>
	            <button onclick="completeOrder()">Confirm Payment</button>
        	</div>
    	</div>
	</div>	
    <script>
	    let orderItems = [];
	
	    function addToOrder(name, price) {
	        let existingItem = orderItems.find(item => item.name === name);
	        if (existingItem) {
	            existingItem.qty++;
	        } else {
	            orderItems.push({ name, price, qty: 1 });
	        }
	        updateOrder();
	    }
	
	    function updateOrder() {
	        const orderItemsContainer = document.getElementById('order-items');
	        let subtotal = 0;
	        orderItemsContainer.innerHTML = '';
	
	        orderItems.forEach((item, index) => {
	            const row = document.createElement('tr');
	            row.innerHTML = `
	                <td>${item.name}</td>
	                <td>
	                    <input type="number" value="${item.qty}" min="1" style="width: 50px;" 
	                           onchange="updateQuantity(${index}, this.value)">
	                </td>
	                <td>RM ${(item.price * item.qty).toFixed(2)}</td>
	                <td>
	                    <button onclick="removeFromOrder(${index})" style="color: red;">X</button>
	                </td>
	            `;
	            orderItemsContainer.appendChild(row);
	            subtotal += item.price * item.qty;
	        });
	
	        document.getElementById('subtotal').textContent = `RM ${subtotal.toFixed(2)}`;
	        document.getElementById('total').textContent = `RM ${subtotal.toFixed(2)}`;
	    }
	
	    function updateQuantity(index, qty) {
	        if (qty < 1) {
	            alert("Quantity must be at least 1.");
	            return;
	        }
	        orderItems[index].qty = parseInt(qty);
	        updateOrder();
	    }
	
	    function removeFromOrder(index) {
	        orderItems.splice(index, 1);
	        updateOrder();
	    }
	
	    function togglePayment(method) {
	        document.querySelectorAll('.payment-details').forEach(section => {
	            section.classList.remove('active');
	        });
	        document.getElementById(method).classList.add('active');
	    }
	
	    function calculateChange() {
	        const moneyReceived = parseFloat(document.getElementById('money-received').value);
	        const total = parseFloat(document.getElementById('total').textContent.replace('RM ', ''));
	        if (!isNaN(moneyReceived) && moneyReceived >= total) {
	            document.getElementById('change').value = (moneyReceived - total).toFixed(2);
	        } else {
	            document.getElementById('change').value = '';
	        }
	    }
	
	    function completeOrder() {
	        if (orderItems.length === 0) {
	            alert("No items in the order. Add products to proceed.");
	            return;
	        }
	        alert("Order Completed!");
	        orderItems = [];
	        updateOrder();
	    }
	</script>
</body>
</html>