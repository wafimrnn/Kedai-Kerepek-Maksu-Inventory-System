<%@ page import="java.util.List" %>
<%@ page import="com.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Details</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="css/viewAccount.css">
    <link rel="stylesheet" type="text/css" href="css/notification.css">
</head>
<body data-role="<%= session.getAttribute("userRole") %>">
    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <div class="nav-links">
            <a href="DashboardHome.jsp">Dashboard</a>
            <a href="ViewProductServlet">Product</a>
            <a href="CreateSales.jsp">Sales</a>
            <a href="Report.html">Report</a>
            <a href="ViewAccount.jsp" class="nav-link active">Account</a>
        </div>
    </div>

    <!-- Head Bar -->
    <div class="head-bar">
        <div class="title">Account Details</div>
        <div class="icons">
            <i class="fas fa-bell" id="notification-icon" title="Notifications"></i>
            <i class="fas fa-user-circle" title="Account"></i>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="blurred-box">
            <!-- Account Details Section -->
            <div class="header">
                <h1>My Account</h1>
                <div class="button-container">
                    <!-- Update Account Button -->
                    <a href="UpdateAccount.jsp" class="add-btn">Update Account</a>

                    <!-- Only show 'Create Staff Account' button for users with 'OWNER' role -->
                    <% if ("OWNER".equals(session.getAttribute("userRole"))) { %>
                        <button class="add-btn" onclick="location.href='CreateStaffAccount.jsp'">Create Staff Account</button>
                    <% } %>

                    <button class="add-btn logout-btn" onclick="location.href='/Kedai-Kerepek-Maksu-Inventory-System/LogoutServlet'">Logout</button>
                </div>
            </div>

            <!-- Display Account Info -->
            <div class="account-info">
                <p><strong>Name:</strong> <%= session.getAttribute("userName") != null ? session.getAttribute("userName") : "No data available" %></p>
                <p><strong>Phone:</strong> <%= session.getAttribute("userPhone") != null ? session.getAttribute("userPhone") : "No data available" %></p>
                <p><strong>Address:</strong> <%= session.getAttribute("userAddress") != null ? session.getAttribute("userAddress") : "No data available" %></p>
                <p><strong>Role:</strong> <%= session.getAttribute("userRole") != null ? session.getAttribute("userRole") : "No data available" %></p>
                <p><strong>Status:</strong> <%= session.getAttribute("accStatus") != null ? session.getAttribute("accStatus") : "No data available" %></p>
            </div>
        </div>
    </div>
    <div id="notification-popup" style="display: none;">
        <ul id="notification-list"></ul>
    </div>
    <script src="js/account.js"></script>
    <script src="js/notification.js"></script>
</body>
</html>