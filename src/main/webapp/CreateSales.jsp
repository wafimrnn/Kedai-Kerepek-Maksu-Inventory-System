<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dao.ProductDAO" %>
<%@ page import="com.model.Product" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sales</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="css/styles.css">
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
	    background-size: cover;
	    background-position: center;
	    position: relative;
	}
	
	/* Sidebar Styling */
	.sidebar {
	    width: 220px;
	    background-color: #481D01;
	    color: white;
	    display: flex;
	    flex-direction: column;
	    padding: 20px;
	    backdrop-filter: blur(10px);
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
	    background-color: #FEECC3;
	    color: black;
	}
	
	.nav-links a.active {
	    background-color: #F6C324;
	    color: black;
	}
	
	/* Head Bar Styling */
	.head-bar {
	    width: calc(100% - 220px);
	    height: 60px;
	    background-color: #F6C324;
	    color: white;
	    display: flex;
	    justify-content: space-between;
	    align-items: center;
	    padding: 0 20px;
	    position: fixed;
	    top: 0;
	    left: 220px;
	    z-index: 1000;
	    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
	    backdrop-filter: blur(10px);
	}
	
	.head-bar .title {
	    font-size: 20px;
	    font-weight: bold;
	    color: black;
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
	
	/* Main Content Layout */
	.main-content {
	    display: flex;
	    justify-content: space-between;
	    align-items: flex-start;
	    flex-wrap: wrap;
	    margin-top: 60px;
	    padding: 20px;
	    width: calc(100% - 220px);
	}
	
	/* Product List Styling */
	.product-list {
	    flex: 2;
	    margin-right: 20px;
	    max-width: calc(100% - 420px);
	}
	
	.product-list h1 {
	    font-size: 28px;
	    color: white;
	    margin-bottom: 20px;
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
	    background-color: #FAD02C;
	    color: white;
	    border: none;
	    padding: 5px 10px;
	    border-radius: 3px;
	    cursor: pointer;
	}
	
	.product-item button:hover {
	    background-color: #F6C324;
	    color: black;
	}
	
	/* Order Calculation Section */
	.order-calculation {
	    flex: 1;
	    background-color: #fff;
	    padding: 20px;
	    border-radius: 5px;
	    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
	    width: 350px;
	    position: relative;
	    top: auto;
	    right: auto;
	}
	
	/* Order Details Styling */
	.order-details table {
	    width: 100%;
	    border-collapse: collapse;
	    margin-bottom: 20px;
	    table-layout: fixed;
	}
	
	.order-details table th,
	.order-details table td {
	    text-align: left;
	    padding: 8px;
	    border-bottom: 1px solid #ddd;
	    font-size: 14px;
	    word-wrap: break-word;
	}
	
	.order-details table th.qty,
	.order-details table td.qty {
	    width: 50px;
	    text-align: center;
	}
	
	.order-details table th.action,
	.order-details table td.action {
	    width: 60px;
	    text-align: center;
	}
	
	.order-details input {
	    width: 40px;
	    text-align: center;
	    font-size: 14px;
	    padding: 2px;
	}
	
	.order-details button {
	    padding: 2px 6px;
	    font-size: 12px;
	    background-color: #FAD02C;
	    color: white;
	    border: none;
	    border-radius: 3px;
	    cursor: pointer;
	}
	
	.order-details button:hover {
	    background-color: #F6C324;
	    color: black;
	}
	
	/* Totals */
	.totals {
	    margin-bottom: 15px;
	    font-size: 14px;
	    display: flex;
	    justify-content: space-between;
	}
	
	/* Payment Method */
	.payment-method {
	    margin-bottom: 15px;
	}
	
	.payment-method button {
	    width: 48%;
	    padding: 10px;
	    margin-right: 2%;
	    background-color: #FAD02C;
	    color: black;
	    border: none;
	    border-radius: 5px;
	    cursor: pointer;
	}
	
	.payment-method button:hover {
	    background-color: #F6C324;
	    color: black;
	}
	
	.payment-method button:last-child {
	    margin-right: 0;
	}
	
	/* Payment Details */
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
	    background-color: #FAD02C;
	    color: black;
	    border: none;
	    border-radius: 5px;
	    cursor: pointer;
	}
	
	.payment-details button:hover {
	    background-color: #F6C324;
	    color: black;
	}
	
	/* Responsive Design */
	@media screen and (max-width: 900px) {
	    .main-content {
	        flex-direction: column;
	        align-items: center;
	    }
	
	    .product-list {
	        max-width: 100%;
	        margin-right: 0;
	    }
	
	    .order-calculation {
	        width: 100%;
	        margin-top: 20px;
	    }
	}
    </style>
    <script>
        const userId = ${sessionScope.userId};
        console.log("Logged in user ID: ", userId);
    </script>
</head>
<body>
    <!-- Sidebar Section -->
    <div class="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <div class="nav-links">
            <a href="DashboardHome.jsp">Dashboard</a>
            <a href="ViewProductServlet">Product</a>
            <a href="CreateSales.jsp" class="active">Sales</a>
            <a href="Report.jsp">Report</a>
            <a href="ViewAccountServlet">Account</a>
        </div>
    </div>

    <!-- Head Bar Section -->
    <div class="head-bar">
        <div class="title">Sales</div>
    </div>

    <div class="main-content">
    
        <!-- Product List Section -->
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
                    <img src="<%= product.getImagePath()%>" alt="<%= product.getProdName() %>">
                    <h4><%= product.getProdName() %></h4>
                    <p>Price: RM <%= product.getProdPrice() %></p>
                    <button class="add-to-order" 
                            data-prodId="<%= product.getProdId() %>" 
                            data-prodName="<%= product.getProdName() %>" 
                            data-prodPrice="<%= product.getProdPrice() %>">
                        Add to Order
                    </button>
                </div>
                <% } %>
            </div>
        </div>

        <!-- Order Calculation Section -->
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
                    <tbody id="order-items"></tbody>
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

            <div id="CASH" class="payment-details">
                <h3>Cash Payment</h3>
                <input type="text" id="money-received" placeholder="Money Received" oninput="calculateChange()">
                <input type="hidden" id="total-amount" value="0">
                <input type="hidden" id="payment-method" value="cash">
                <input type="text" id="change" placeholder="Change" disabled>
                <button onclick="completeOrder()">Complete Order</button>
            </div>

            <div id="QR" class="payment-details">
                <h3>QR Payment</h3>
                <input type="hidden" id="total-amount" value="0">
                <input type="hidden" id="payment-method" value="qr">
                <button onclick="completeOrder()">Confirm Payment</button>
            </div>

            <div id="response-message" style="display: none; margin-top: 10px; font-size: 16px;">
                Order Completed Successfully!
            </div>

            <!-- Generate Receipt Button -->
            
        </div>
    </div>

    <script>
        const contextPath = "<%= request.getContextPath() %>";
    </script>
    <script src="pos.js"></script>
</body>
</html>
