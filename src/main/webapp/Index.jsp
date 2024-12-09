<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
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
        .card {
            background-color: white;
            border: 1px solid #ddd;
            padding: 15px;
            border-radius: 5px;
            margin: 10px 0;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        .card h3 {
            margin: 0 0 10px 0;
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
        .logout {
            background-color: #dc3545;
        }
        .logout:hover {
            background-color: #a71d2a;
        }
    </style>
</head>
<body>
    <div class="navbar">
        <h1>Welcome to the Kedai Kerepek Maksu</h1>
        <a href="logout.jsp" class="button logout">Logout</a>
    </div>
    <div class="container">
        <div class="card">
            <h3>Create Products</h3>
            <p>Check and manage product inventory.</p>
            <a href="createProduct.jsp" class="button">Go to Products</a>
        </div>
        <div class="card">
            <h3>View Product</h3>
            <p>Add new products to the inventory.</p>
            <a href="viewProducts.jsp" class="button">Add New Product</a>
        </div>
        <div class="card">
            <h3>Update Products</h3>
            <p>Update products inventory.</p>
            <a href="updateProduct.jsp" class="button">Update Products</a>
        </div>
        <div class="card">
            <h3>Delete Products</h3>
            <p>Delete products inventory.</p>
            <a href="deleteProduct.jsp" class="button">Delete Products</a>
        </div>
    </div>s
</body>
</html>
