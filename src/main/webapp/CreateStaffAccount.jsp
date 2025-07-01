<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Staff Account</title>
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
            background-image: url('img/pisangImage.jpg');
            background-size: cover;
            background-position: center;
            position: relative;
        }

        .sidebar {
            width: 220px;
            background-color: #481D01;
            color: white;
            display: flex;
            flex-direction: column;
            padding: 20px;
            backdrop-filter: blur(10px);
        }

        .sidebar h2 {
            margin-bottom: 20px;
            text-align: center;
            font-size: 20px;
            border-bottom: 1px solid #495057;
            padding-bottom: 10px;
        }

        .nav-links a {
            text-decoration: none;
            color: white;
            padding: 10px 15px;
            margin: 5px 0;
            border-radius: 4px;
            transition: background 0.3s ease;
        }

        .nav-links a:hover {
            background-color: #FEECC3;
            color: black;
        }

        .main-content {
            flex: 1;
            padding: 20px;
            margin-top: 60px;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .header {
            margin-bottom: 20px;
            border-bottom: 2px solid #ccc;
            padding-bottom: 10px;
        }

        .header h1 {
            font-size: 28px;
            color: #343a40;
        }

        .form-container {
            background-color: #FBE39D;
            padding: 30px;
            border-radius: 10px;
            width: 80%;
            max-width: 500px;
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

        .form-group input {
            width: 100%;
            padding: 10px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .form-buttons {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }

        .form-buttons button {
            padding: 10px 15px;
            border: none;
            border-radius: 5px;
            font-size: 14px;
            cursor: pointer;
        }

        .submit-btn {
            background-color: #28a745;
            color: white;
        }

        .cancel-btn {
            background-color: #dc3545;
            color: white;
        }

        .error {
            margin-top: 20px;
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <div class="nav-links">
            <a href="DashboardHome.jsp">Dashboard</a>
            <a href="ViewProductServlet">Product</a>
            <a href="CreateSales.jsp">Sales</a>
            <a href="Report.jsp">Report</a>
            <a href="ViewAccountServlet">Account</a>
        </div>
    </div>

    <div class="main-content">
        <div class="header">
            <h1>Create Staff Account</h1>
        </div>

        <div class="form-container">
            <form id="createStaffForm" action="CreateStaffServlet" method="post" onsubmit="return validateForm()">
                <div class="form-group">
                    <label for="userName">Username:</label>
                    <input type="text" id="userName" name="userName" required>
                </div>

                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required minlength="6">
                </div>

                <div class="form-group">
                    <label for="phone">Phone:</label>
                    <input type="tel" id="phone" name="phone" pattern="[0-9]{10,15}" title="Enter 10 to 15 digits only" required>
                </div>

                <div class="form-group">
                    <label for="address">Address:</label>
                    <input type="text" id="address" name="address" required>
                </div>

                <input type="hidden" name="ownerId" value="${sessionScope.userId}">

                <div class="form-buttons">
                    <button type="submit" class="submit-btn">Create Account</button>
                    <button type="button" class="cancel-btn" onclick="window.location.replace('ViewAccount.jsp');">Cancel</button>
                </div>
            </form>
        </div>

        <% String errorMessage = (String) request.getAttribute("error");
           if (errorMessage != null) { %>
            <div class="error"><%= errorMessage %></div>
        <% } %>
    </div>

    <script>
        function validateForm() {
            const phone = document.getElementById("phone").value;
            const phonePattern = /^[0-9]{10,15}$/;

            if (!phonePattern.test(phone)) {
                alert("Please enter a valid phone number (10 to 15 digits).");
                return false;
            }

            return true;
        }
    </script>
</body>
</html>