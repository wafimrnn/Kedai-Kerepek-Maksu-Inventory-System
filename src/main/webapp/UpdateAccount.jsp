<%@ page import="com.model.Account" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Account</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
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
            background-color: #f4f4f9;
        }

        /* Sidebar */
        .sidebar {
            width: 220px;
            background-color: #343a40;
            color: white;
            padding: 20px;
        }

        .sidebar h2 {
            text-align: center;
            border-bottom: 1px solid #495057;
            padding-bottom: 10px;
        }

        .sidebar a {
            display: block;
            color: white;
            text-decoration: none;
            padding: 10px;
            border-radius: 4px;
            transition: 0.3s;
        }

        .sidebar a.active, .sidebar a:hover {
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

        .main-content {
            flex: 1;
            padding: 20px;
            margin-top: 60px; /* Push content below the head bar */
        }

        .form-container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .form-group {
            margin-bottom: 15px;
        }
        
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }

        input, select {
            width: 100%;
            padding: 10px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        button {
            padding: 10px 15px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .cancel-button {
            background-color: #dc3545;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Dashboard</h2>
        <a href="ViewAccountServlet" class="active">Account</a>
        <a href="ViewProductServlet">Product</a>
        <a href="CreateSales.jsp">Sales</a>
        <a href="Report.html">Report</a>
    </div>
    <!-- Head Bar -->
    <div class="head-bar">
        <div class="title">Account Management</div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <h2>Update Account</h2>
        <div class="form-container">
            <%
                Account account = (Account) request.getAttribute("account");
                if (account == null) {
            %>
                <p style="color:red;">Account not found.</p>
            <% } else { %>
            <form action="UpdateAccountServlet" method="post">
                <input type="hidden" name="accountId" value="<%= account.getAccountId() %>">

                <div class="form-group">
                    <label for="username">Username:</label>
                    <input type="text" id="username" name="username" value="<%= account.getUsername() %>" required>
                </div>

                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" value="<%= account.getEmail() %>" required>
                </div>

                <div class="form-group">
                    <label for="status">Account Status:</label>
                    <select id="status" name="status" required>
                        <option value="Active" <%= "Active".equals(account.getStatus()) ? "selected" : "" %>>Active</option>
                        <option value="Inactive" <%= "Inactive".equals(account.getStatus()) ? "selected" : "" %>>Inactive</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required>
                </div>

                <button type="submit">Update Account</button>
                <button type="button" class="cancel-button" onclick="window.location.href='ViewAccountServlet'">Cancel</button>
            </form>
            <% } %>
        </div>
    </div>
</body>
</html>