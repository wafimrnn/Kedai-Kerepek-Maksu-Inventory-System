<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Account</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="css/updateAccount.css">
</head>
<body>
    <div class="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <div class="nav-links">
            <a href="DashboardHome.jsp">Dashboard</a>
            <a href="ViewProductServlet">Product</a>
            <a href="CreateSales.jsp">Sales</a>
            <a href="Report.jsp">Report</a>
            <a href="ViewAccountServlet" class="active">Account</a>
        </div>
    </div>

    <div class="head-bar">
        <div>Account Management</div>
        <div class="icons">
            <i class="fas fa-user-circle"></i>
        </div>
    </div>

    <div class="main-content">
        <div class="blurred-box">
            <h2>Update Account</h2>

            <% String message = (String) request.getAttribute("message"); %>
            <% String error = (String) request.getAttribute("error"); %>

            <% if (message != null) { %>
                <p class="message" style="color: green;"><%= message %></p>
            <% } %>
            <% if (error != null) { %>
                <p class="message" style="color: red;"><%= error %></p>
            <% } %>

            <div class="form-container">
                <form action="UpdateAccountServlet" method="post">
                    <input type="hidden" name="userId" value="${userId}" />

                    <label for="username">Username:</label>
                    <input type="text" id="username" name="username" value="${username}" required />

                    <label for="phone">Phone:</label>
                    <input type="tel" id="phone" name="phone" value="${phone}" pattern="[0-9]{10,15}" title="Please enter 10 to 15 digits" required />

                    <label for="address">Address:</label>
                    <input type="text" id="address" name="address" value="${address}" required />

                    <label for="role">Role:</label>
                    <select id="role" name="role" required>
                        <option value="OWNER" ${role == 'OWNER' ? 'selected' : ''}>OWNER</option>
                        <option value="STAFF" ${role == 'STAFF' ? 'selected' : ''}>STAFF</option>
                    </select>

                    <label for="status">Account Status:</label>
                    <select id="status" name="status" required>
                        <option value="active" ${status == 'active' ? 'selected' : ''}>Active</option>
                        <option value="inactive" ${status == 'inactive' ? 'selected' : ''}>Inactive</option>
                    </select>

                    <button type="submit" class="submit-button">Update Account</button>
                    <button type="button" class="cancel-button" onclick="window.location.href='ViewAccountServlet'">Cancel</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
