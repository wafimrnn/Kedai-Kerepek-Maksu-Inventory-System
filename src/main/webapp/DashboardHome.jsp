<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>
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
            background-color: #f4f4f9;
        }

        /* Sidebar Styling */
        .sidebar {
            width: 220px;
            background-color: #343a40;
            color: white;
            display: flex;
            flex-direction: column;
            padding: 20px;
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
            color: white;
            padding: 10px 15px;
            margin: 5px 0;
            border-radius: 4px;
            transition: background 0.3s ease;
        }

        .nav-links a:hover {
            background-color: #495057;
        }

        .nav-links a.active {
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
		
		.head-bar .icons {
		    display: flex;
		    align-items: center;
		    gap: 15px;
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
            background-color: white;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            margin-top: 60px; /* Push content below the head bar */
        }

        .main-content h1 {
            font-size: 28px;
            color: #343a40;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <div class="nav-links">
            <a href="DashboardHome.jsp" class="active">Dashboard</a>
            <a href="ViewProductServlet">Product</a>
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

    <!-- Main Content -->
    <div class="main-content">
        <h1>Welcome to the Dashboard</h1>
    </div>
</body>
</html>
