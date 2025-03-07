<%@ page import="com.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Account</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="css/styles.css">
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
            background-image: url('img/pisangImage.jpg'); /* Background image */
            background-size: cover; /* Cover the entire area */
            background-position: center; /* Center the image */
            position: relative;
        }

        /* Sidebar Styling */
        .sidebar {
            width: 220px;
            background-color: #481D01; /* Semi-transparent background color */
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
            background-color: #FEECC3;
            color: black;
        }

        .nav-links a.active {
            background-color: #F6C324; /* Background color for active link */
            color: black; /* Font color for active link */
        }
        
        /* Head Bar Styling */
        .head-bar {
            width: calc(100% - 220px); /* Full width minus the sidebar width */
            height: 60px;
            background-color: #F6C324; /* Semi-transparent background color */
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
            color: black;
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
            justify-content: center;
            margin-top: 60px; /* Push content below the head bar */
            overflow: hidden; /* Prevent overflow */
        }
        
        .main-content::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-image: url('img/pisangImage.jpg'); /* Path to your image */
            background-size: cover; /* Cover the entire area */
            background-position: center; /* Center the image */
            background-repeat: no-repeat; /* Prevent the image from repeating */
            filter: blur(1px); /* Adjust blur intensity */
            z-index: -1; /* Push background below all content */
        }
        
        .main-content h2{
        text-align: center;
        }
        
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            border-bottom: 2px solid black;
            padding-bottom: 10px;
        }

        .header h2 {
            font-size: 28px;
            color: #343a40;
        }
        
        .blurred-box {
            position: relative;
            z-index: 1;
            padding: 40px;
            background: #FBE39D;
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

        /* Form Styling */
        .form-container {
            background-color: #f9f9f9;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
            max-width: 500px;
            margin: 0 auto;
            box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            font-weight: bold;
            margin-bottom: 5px;
            color: #555;
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
             margin-left: 250px;
        }
    </style>
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
        <div class="icons">
	        <i id="notification-icon" class="fas fa-bell" title="Notifications"></i>
	        <i class="fas fa-user-circle" title="Account"></i>
	    </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="blurred-box">
            <h2>Update Account</h2>

            <!-- Feedback Messages -->
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
                <form action="UpdateAccountServlet" method="post">
                    <!-- Hidden User ID Field -->
                    <input type="hidden" name="userId" value="${userId}" />

                    <div class="form-group">
                        <label for="username">Username:</label>
                        <input 
                            type="text" 
                            id="username" 
                            name="username" 
                            value="${username}">
                    </div>
                    
                    <div class="form-group">
                        <label for="phone">Phone:</label>
                        <input 
                            type="text" 
                            id="phone" 
                            name="phone" 
                            value="${phone}">
                    </div>
                    
                    <div class="form-group">
                        <label for="address">Address:</label>
                        <input 
                            type="text" 
                            id="address" 
                            name="address" 
                            value="${address}">
                    </div>
                    
                    <div class="form-group">
                        <label for="role">Role:</label>
                        <select id="role" name="role" required>
						    <option value="OWNER">OWNER</option>
						    <option value="STAFF">STAFF</option>
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
    <div id="notification-popup">
    <ul id="notification-list"></ul>
</div>
    <script src="js/notification.js"></script>
</body>
</html>