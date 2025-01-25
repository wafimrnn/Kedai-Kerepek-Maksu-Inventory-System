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
            justify-content: center;
            margin-top: 60px; /* Push content below the head bar */
            overflow: hidden; /* Prevent overflow */
        }
        
        .main-content h2 {
      		font-size: 30px;
      		letter-spacing: 10px;
      		margin-bottom: 20px;
      		font-weight: bold;
      		position: relative;
      		z-index: 2;
      		color: #545445;
    	}

    	/* Kedai Kerepek Maksu Heading */
    		.main-content h1 {
      		font-size: 60px;
      		font-weight: bold;
      		margin-bottom: 90px;
      		position: relative;
      		z-index: 2;
      		color: #545445;
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
         
         /* Blurry Box */
         .blurred-box {
            position: relative;
            z-index: 1;
            padding: 40px;
            background: rgba(255, 250, 171, 0.62);
            backdrop-filter: blur(8px); /* Ensure this is applied correctly */
            border-radius: 10px;
            margin-top: 20px height: 200px; /* Set a height to center vertically */
            margin-bottom: 20px;
            display: flex; /* Use flexbox */
            flex-direction: column; /* Stack items vertically */
            justify-content: center; /* Center content vertically */
            align-items: center; /* Center content horizontally */
            text-align: center; /* Center text horizontally */
        }
        
        #notification-popup {
		    position: absolute;
		    top: 50px; /* Adjust based on your layout */
		    right: 20px;
		    background: white;
		    border: 1px solid #ccc;
		    border-radius: 5px;
		    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
		    padding: 10px;
		    z-index: 1000;
		    max-width: 300px;
		    display: none;
		}
		
		#notification-popup ul {
		    list-style: none;
		    margin: 0;
		    padding: 0;
		}
		
		#notification-popup li {
		    margin: 5px 0;
		    padding: 5px;
		    border-bottom: 1px solid #eee;
		}
		
		#notification-popup li:last-child {
		    border-bottom: none;
		}
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <div class="nav-links">
            <a href="DashboardHome.jsp" class="active">Dashboard</a>
            <a href="ViewProduct.jsp">Product</a>
            <a href="CreateSales.jsp">Sales</a>
            <a href="Report.html">Report</a>
            <a href="ViewAccount.jsp">Account</a>
        </div>
    </div>
    <!-- Head Bar -->
	<div class="head-bar">
	    <div class="title">Dashboard</div>
	    <div class="icons">
	        <i class="fas fa-bell" id="notification-icon" title="Notifications"></i>
	        <i class="fas fa-user-circle" title="Account"></i>
	    </div>
	</div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="blurred-box">
        	<h2>WELCOME TO DASHBOARD</h2>
            <h1>KEDAI KEREPEK MAKSU</h1>
        </div>
    </div>
    
    <div id="notification-popup" style="display: none;">
	    <ul id="notification-list"></ul>
	</div>
</body>
</html>