<%@ page import="java.sql.*, java.util.*, com.project.DBConnection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Kedai Kerepek Maksu</title>
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

        .top-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .top-bar button {
            background-color: #ffc107;
            border: none;
            padding: 10px 15px;
            color: white;
            border-radius: 5px;
            cursor: pointer;
        }

        .top-bar button:hover {
            background-color: #e0a800;
        }

        .summary {
            display: flex;
            justify-content: space-around;
            margin-bottom: 20px;
        }

        .summary-item {
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            padding: 20px;
            width: 30%;
            text-align: center;
        }

        .summary-item h3 {
            font-size: 1.5em;
            margin-bottom: 10px;
        }

        .inventory {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
        }

        .inventory-item {
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            padding: 10px;
            text-align: center;
        }

        .inventory-item img {
            width: 100%;
            border-radius: 10px;
        }

        .inventory-item h3 {
            margin: 10px 0;
            font-size: 1.1em;
        }

        .buttons {
            display: flex;
            justify-content: space-around;
        }

        .buttons button {
            padding: 5px 10px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        
        .black-button {
            padding: 10px 20px;
            background-color: #e0a800;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: bold;
            text-transform: uppercase;
        }

        .black-button:hover {
            background-color: #444;
        }
    </style>
</head>
<body>
    <div class="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <ul>
            <li><a href="Dashboard.jsp" class="active">Dashboard</a></li>
            <li><a href="#">Inventories</a></li>
            <li><a href="#">Reports</a></li>
            <li><a href="#">Profile Account</a></li>
            <li><a href="#">Log Out</a></li>
        </ul>
    </div>

    <div class="content">
        <div class="top-bar">
            <a href="CreateProduct.html" class="black-button">Add Product</a>
            <a href="#" class="black-button">Create Sale</a>
        </div>

        <!-- Sales and Inventory Summary -->
        <%
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    double totalSales = 0;
    int totalProducts = 0;

    try {
        // Establish database connection
        conn = DBConnection.getConnection();

        // Query for total sales
        String totalSalesQuery = "SELECT SUM(totalAmount) FROM Orders";
        stmt = conn.prepareStatement(totalSalesQuery);
        rs = stmt.executeQuery();
        if (rs.next()) {
            totalSales = rs.getDouble(1);
        }

        // Close the previous statement
        if (stmt != null) stmt.close();

        // Query for total products in stock
        String totalProductsQuery = "SELECT SUM(stockQuantity) FROM Product";
        stmt = conn.prepareStatement(totalProductsQuery);
        rs = stmt.executeQuery();
        if (rs.next()) {
            totalProducts = rs.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
        if (stmt != null) try { stmt.close(); } catch (SQLException ignored) {}
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
    }
%>

	<div class="summary">
    	<div class="summary-item">
        	<h3>Total Sales</h3>
        	<p><%= String.format("%.2f", totalSales) %> USD</p>
    	</div>
    	<div class="summary-item">
        	<h3>Total Products in Stock</h3>
        	<p><%= totalProducts %> items</p>
    	</div>
	</div>
	<div class="inventory">
	<%
    try {
        conn = DBConnection.getConnection();
        String productQuery = "SELECT productId, productName, imagePath, stockQuantity FROM Product";
        stmt = conn.prepareStatement(productQuery);
        rs = stmt.executeQuery();

        while (rs.next()) {
            int productId = rs.getInt("productId");
            String productName = rs.getString("productName");
            String imagePath = rs.getString("imagePath");
            int stockQuantity = rs.getInt("stockQuantity");
	%>
            <div class="inventory-item">
                <img src="<%= imagePath != null ? imagePath : "default-image.jpg" %>" alt="<%= productName %>" style="height:150px; width:150px;">
                <h3><%= productName %></h3>
                <p>Stock: <%= stockQuantity %></p>
                <div class="buttons">
                    <a href="EditProduct.jsp?productId=<%= productId %>" class="black-button">Edit</a>
                    <a href="DeleteProductServlet?id=<%= productId %>" class="black-button">Delete</a>
                </div>
            </div>
	<%
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
        if (stmt != null) try { stmt.close(); } catch (SQLException ignored) {}
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
    }
	%>
	</div>  
    </div>
</body>
</html>