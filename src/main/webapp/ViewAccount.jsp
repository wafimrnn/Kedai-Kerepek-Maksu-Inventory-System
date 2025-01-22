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
        /* Basic styling for account table */
        body {
            font-family: Arial, sans-serif;
        }
        .table-container {
            margin: 20px auto;
            max-width: 900px;
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        th, td {
            padding: 10px;
            text-align: left;
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
            <a href="ViewProductServlet" class="nav-link active">Product</a>
            <a href="CreateSales.jsp">Sales</a>
            <a href="Report.html">Report</a>
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

</body>
</html>
