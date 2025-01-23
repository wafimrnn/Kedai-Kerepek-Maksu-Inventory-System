<%@ page import="com.model.Account" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dao.AccountDAO" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Account</title>
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

        /* Main Content Area */
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
        
        .blurred-box {
            position: relative;
            z-index: 1;
            padding: 40px;
            background: rgba(255, 250, 171, 0.62);
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

        /* Account Table Styling */
        /* Table Styling */
        .table-container {
            margin-top: 20px;
        }

        .table-container h2 {
            font-size: 24px;
            margin-bottom: 15px;
            color: #343a40;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }

        table, th, td {
            border: 1px solid #ccc;

        table {
            width: 100%;
            border-collapse: collapse;
            background-color: white;
        }

        th, td {
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #f1f1f1;
        }

        .action-button {
            background-color: #007BFF;
            color: white;
            padding: 8px 15px;
            border-radius: 5px;
            text-decoration: none;
            transition: background 0.3s ease;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #007BFF;
            color: white;
        }

        .action-button {
            padding: 10px 15px;
            background-color: #007BFF;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
        }

        .action-button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

<!-- Sidebar -->
<div class="sidebar">
    <h2>Kedai Kerepek Maksu</h2>
    <div class="nav-links">
        <a href="DashboardHome.jsp">Dashboard</a>
        <a href="ViewProduct.jsp">Product</a>
        <a href="CreateSales.jsp">Sales</a>
        <a href="Report.html">Report</a>
        <a href="ViewAccount.jsp" class="active">Account</a>
    </div>
</div>

<!-- Head Bar -->
<div class="head-bar">
    <div class="title">Account</div>
    <div class="icons">
        <i class="fas fa-bell" title="Notifications"></i>
        <i class="fas fa-user-circle" title="Account"></i>
    </div>
</div>

<!-- Main Content -->
<div class="main-content">
<div class="blurred-box">
    <!-- Account Table -->
    <div class="table-container">
        <h2>Account List</h2>

        <%
            List<Account> accounts = (List<Account>) request.getAttribute("accounts");
            if (accounts != null && !accounts.isEmpty()) {
        %>

        <table>
            <thead>
                <tr>
                    <th>Account ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Account account : accounts) {
                %>
                <tr>
                    <td><%= account.getAccountId() %></td>
                    <td><%= account.getUsername() %></td>
                    <td><%= account.getEmail() %></td>
                    <td><%= account.getStatus() %></td>
                    <td>
                        <a href="UpdateAccountServlet?accountId=<%= account.getAccountId() %>" class="action-button">Update Account</a>
                    </td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>

        <%
            } else {
        %>
        <p>No accounts available.</p>
        <%
            }
        %>
    </div>
</div>
</div>
</body>
</html>
