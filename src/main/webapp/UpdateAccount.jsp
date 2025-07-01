<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Account</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="css/updateAccount.css">
</head>
<body>
    <!-- Sidebar -->
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

    <!-- Head Bar -->
    <div class="head-bar">
        <div class="title">Account Management</div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="blurred-box">
            <h2>Update Account</h2>

            <% String message = (String) request.getAttribute("message");
               String error = (String) request.getAttribute("error");
            %>

            <% if (message != null) { %>
                <div style="color: green;"><%= message %></div>
            <% } %>
            <% if (error != null) { %>
                <div style="color: red;"><%= error %></div>
            <% } %>

            <div class="form-container">
                <form id="updateAccountForm" action="UpdateAccountServlet" method="post" onsubmit="return validateForm()">
                    <input type="hidden" name="userId" value="${userId}" />

                    <div class="form-group">
                        <label for="username">Username:</label>
                        <input 
                            type="text" 
                            id="username" 
                            name="username" 
                            value="${username}" 
                            required>
                    </div>

                    <div class="form-group">
                        <label for="phone">Phone:</label>
                        <input 
                            type="tel" 
                            id="phone" 
                            name="phone" 
                            value="${phone}" 
                            pattern="[0-9]{10,15}" 
                            title="Enter a valid phone number (10–15 digits)" 
                            required>
                    </div>

                    <div class="form-group">
                        <label for="address">Address:</label>
                        <input 
                            type="text" 
                            id="address" 
                            name="address" 
                            value="${address}" 
                            required>
                    </div>

                    <div class="form-group">
                        <label for="role">Role:</label>
                        <select id="role" name="role" required>
                            <option value="OWNER" ${role == 'OWNER' ? 'selected' : ''}>OWNER</option>
                            <option value="STAFF" ${role == 'STAFF' ? 'selected' : ''}>STAFF</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="status">Account Status:</label>
                        <select id="status" name="status" required>
                            <option value="active" ${status == 'active' ? 'selected' : ''}>Active</option>
                            <option value="inactive" ${status == 'inactive' ? 'selected' : ''}>Inactive</option>
                        </select>
                    </div>

                    <button type="submit">Update Account</button>
                    <button 
                        type="button" 
                        class="cancel-button" 
                        onclick="window.location.href='ViewAccountServlet'">Cancel</button>
                </form>
            </div>
        </div>
    </div>

    <!-- JS Validation -->
    <script>
        function validateForm() {
            const phone = document.getElementById("phone").value;
            const phonePattern = /^[0-9]{10,15}$/;

            if (!phonePattern.test(phone)) {
                alert("Please enter a valid phone number with 10 to 15 digits.");
                return false;
            }

            return true;
        }
    </script>
</body>
</html>