<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.Connection, java.sql.DriverManager, java.sql.PreparedStatement" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add Product</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 500px;
            margin: 50px auto;
            background: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        input, button {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
        }
        button {
            background-color: #007bff;
            color: white;
            border: none;
            cursor: pointer;
        }
        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Add New Product</h1>
        <form method="post">
            <input type="text" name="product_name" placeholder="Product Name" required>
            <input type="number" name="price" placeholder="Price (RM)" required>
            <input type="number" name="stock" placeholder="Stock" required>
            <button type="submit">Add Product</button>
        </form>
        <%
            if (request.getMethod().equalsIgnoreCase("POST")) {
                String name = request.getParameter("product_name");
                String price = request.getParameter("price");
                String stock = request.getParameter("stock");

                String dbURL = "jdbc:mysql://localhost:3306/kerepek_maksu";
                String dbUser = "root";
                String dbPass = "password";

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass);

                    String query = "INSERT INTO products (product_name, price, stock) VALUES (?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, name);
                    stmt.setDouble(2, Double.parseDouble(price));
                    stmt.setInt(3, Integer.parseInt(stock));

                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        out.println("<p style='color: green;'>Product added successfully!</p>");
                    }

                    stmt.close();
                    conn.close();
                } catch (Exception e) {
                    out.println("<p style='color: red;'>Error: " + e.getMessage() + "</p>");
                }
            }
        %>
    </div>
</body>
</html>
