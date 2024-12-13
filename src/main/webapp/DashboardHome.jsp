<%@ page import="jakarta.servlet.*, java.sql.*, com.project.DBConnection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Kedai Kerepek Maksu</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
        }

        nav {
            background-color: #2c3e50;
            width: 200px;
            height: 100vh;
            padding-top: 20px;
            position: fixed;
        }

        nav ul {
            list-style-type: none;
            padding: 0;
        }

        nav ul li {
            padding: 10px;
            text-align: center;
        }

        nav ul li a {
            color: white;
            text-decoration: none;
            display: block;
            padding: 10px;
            background-color: #34495e;
            margin: 5px 0;
            border-radius: 4px;
        }

        nav ul li a:hover {
            background-color: #16a085;
        }

        .container {
            margin-left: 220px;
            padding: 20px;
            width: 100%;
        }

        h1, h2 {
            color: #2c3e50;
        }

        section {
            margin-bottom: 30px;
        }

        .button {
            padding: 10px 20px;
            background-color: #16a085;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            margin: 10px 0;
        }

        .button:hover {
            background-color: #1abc9c;
        }

        .inventory-item {
            padding: 10px;
            background-color: #ecf0f1;
            border-radius: 4px;
            margin-bottom: 10px;
        }

        .inventory-item h3 {
            margin: 0;
        }

        .inventory-item p {
            margin: 5px 0;
        }

        .inventory-item a {
            margin-right: 10px;
            color: #16a085;
        }
    </style>
</head>
<body>

    <nav>
        <ul>
            <li><a href="Dashboard.jsp">Dashboard</a></li>
            <li><a href="ViewProduct.jsp">Inventories</a></li>
            <li><a href="Reports.jsp">Reports</a></li>
            <li><a href="ProfileAccount.jsp">Profile Account</a></li>
            <li><a href="Logout.jsp">Log Out</a></li>
        </ul>
    </nav>

    <div class="container">
        <h1>Welcome to Kedai Kerepek Maksu</h1>

        <a href="AddProduct.jsp" class="button">Add Product</a>
        <a href="CreateSale.jsp" class="button">Create Sale</a>

        <!-- Summary Section -->
        <section>
            <h2>Summary</h2>
            <%
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            double totalSales = 0;
            int totalProducts = 0;

            try {
                conn = DBConnection.getConnection();

                String totalSalesQuery = "SELECT SUM(totalAmount) FROM Orders";
                stmt = conn.prepareStatement(totalSalesQuery);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    totalSales = rs.getDouble(1);
                }

                stmt.close();
                rs.close();

                String totalProductsQuery = "SELECT SUM(stockQuantity) FROM Product";
                stmt = conn.prepareStatement(totalProductsQuery);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    totalProducts = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
                if (stmt != null) try { stmt.close(); } catch (SQLException ignored) {}
                if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
            }
            %>

            <p>Total Sales: <%= String.format("%.2f", totalSales) %> RM</p>
            <p>Total Products in Stock: <%= totalProducts %> items</p>
        </section>

        <!-- Inventory Section -->
        <section>
            <h2>Inventory</h2>
            <%
            try {
                conn = DBConnection.getConnection();
                String productQuery = "SELECT productId, productName, stockQuantity FROM Product";
                stmt = conn.prepareStatement(productQuery);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    int productId = rs.getInt("productId");
                    String productName = rs.getString("productName");
                    int stockQuantity = rs.getInt("stockQuantity");
            %>
                    <div class="inventory-item">
                        <h3><%= productName %></h3>
                        <p>Stock: <%= stockQuantity %></p>
                        <a href="EditProduct.jsp?productId=<%= productId %>">Edit</a>
                        <a href="DeleteProductServlet?id=<%= productId %>">Delete</a>
                    </div>
            <%
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
                if (stmt != null) try { stmt.close(); } catch (SQLException ignored) {}
                if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
            }
            %>
        </section>
    </div>

</body>
</html>