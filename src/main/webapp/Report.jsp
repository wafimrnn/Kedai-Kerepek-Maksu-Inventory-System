<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Report Sales</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="css/styles.css">
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
            border-bottom: 2px solid black;
            padding-bottom: 10px;
        }

        .header h1 {
            font-size: 28px;
            color: #343a40;
        }

        .main-content h1 {
            font-size: 28px;
            color: #343a40;
            margin-bottom: 20px;
        }

        .report-table {
            width: 100%;
            margin-top: 30px;
            border-collapse: collapse;
        }

        .report-table th, .report-table td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: left;
        }

        .report-table th {
            background-color: #F6C324;
            color: black;
        }

        .report-table td {
            background-color: #FBE39D;
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
            <a href="Report.jsp" class="active">Report</a>
            <a href="ViewAccountServlet">Account</a>
        </div>
    </div>

    <!-- Head Bar -->
    <div class="head-bar">
        <div class="title">Report</div>
        <div class="icons">
            <i id="notification-icon" class="fas fa-bell" title="Notifications"></i>
            <i class="fas fa-user-circle" title="Account"></i>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="blurred-box">
            <h1>Generate Report</h1>

            <!-- Report Form -->
            <div class="report-form">
                <form id="reportForm" action="ReportController" method="post">
                    <label for="reportType">Report Type:</label>
                    <select name="reportType" id="reportType" required>
                        <option value="sales" <%= "sales".equals(request.getAttribute("selectedReportType")) ? "selected" : "" %>>Sales Report</option>
                        <option value="inventory" <%= "inventory".equals(request.getAttribute("selectedReportType")) ? "selected" : "" %>>Inventory Report</option>
                    </select>

                    <label for="startDate">Start Date:</label>
                    <input type="date" name="startDate" id="startDate" required>

                    <label for="endDate">End Date:</label>
                    <input type="date" name="endDate" id="endDate" required>

                    <button type="submit">Generate Report</button>
                </form>
            </div>

            <!-- Report Results -->
            <div id="salesReportSection" style="display: none;">
                <h2><br>Sales Report</h2>
                <table class="report-table">
                    <thead>
                        <tr>
                            <th>Sale ID</th>
                            <th>Sale Date</th>
                            <th>Total Amount</th>
                            <th>Payment Method</th>
                        </tr>
                    </thead>
                    <tbody id="salesReportBody">
                        <%= request.getAttribute("salesReportData") != null ? request.getAttribute("salesReportData") : "" %>
                    </tbody>
                </table>
            </div>

            <div id="inventoryReportSection" style="display: none;">
                <h2><br>Inventory Report</h2>
                <table class="report-table">
                    <thead>
                        <tr>
                            <th>Product Name</th>
			                <th>Product Price</th>
			                <th>Quantity in Stock</th>
                        </tr>
                    </thead>
                    <tbody id="inventoryReportBody">
                        <%= request.getAttribute("inventoryReportData") != null ? request.getAttribute("inventoryReportData") : "" %>
                    </tbody>
                </table>
            </div>

            <!-- Message when no data is available -->
            <p id="noDataMessage" style="display: none;"><br>No report data available for the selected period.</p>
        </div>
    </div>
    <div id="notification-popup">
    <ul id="notification-list"></ul>
</div>
    <!-- Include External JavaScript -->
    <script src="report.js"></script>
    <script src="js/notification.js"></script>
</body>
</html>