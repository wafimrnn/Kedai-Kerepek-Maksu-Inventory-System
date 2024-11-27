<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Products</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f5f5f5;
        }
        .navbar {
            background-color: #007bff;
            color: white;
            padding: 10px 15px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .navbar h1 {
            margin: 0;
            font-size: 20px;
        }
        .navbar a {
            color: white;
            text-decoration: none;
            margin-left: 15px;
        }
        .container {
            padding: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #007bff;
            color: white;
        }
        .button {
            background-color: #007bff;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 5px;
            text-decoration: none;
            font-size: 14px;
            margin: 5px 0;
            cursor: pointer;
        }
        .button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="navbar">
        <h1>Product Inventory</h1>
        <a href="Index.jsp" class="button">Back to Dashboard</a>
    </div>
    <div class="container">
        <h2>Available Products</h2>
        <table>
            <thead>
                <tr>
                    <th>Product ID</th>
                    <th>Product Name</th>
                    <th>Price (RM)</th>
                    <th>Stock</th>
                </tr>
            </thead>
            <tbody>
                <%
                    // Database connection details
                    String dbURL = "jdbc:mysql://localhost:3306/kerepek_maksu";
                    String dbUser = "root";
                    String dbPass = "password";

                    try {
                        // Establish connection
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass);

                        // Query to fetch products
                        String query = "SELECT product_id, product_name, price, stock FROM products";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        ResultSet rs = stmt.executeQuery();

                        // Loop through results
                        while (rs.next()) {
                %>
                <tr>
                    <td><%= rs.getInt("product_id") %></td>
                    <td><%= rs.getString("product_name") %></td>
                    <td><%= rs.getDouble("price") %></td>
                    <td><%= rs.getInt("stock") %></td>
                </tr>
                <%
                        }

                        // Close connections
                        rs.close();
                        stmt.close();
                        conn.close();
                    } catch (Exception e) {
                        out.println("<tr><td colspan='4'>Error fetching products: " + e.getMessage() + "</td></tr>");
                    }
                %>
            </tbody>
        </table>
    </div>
</body>
</html>
