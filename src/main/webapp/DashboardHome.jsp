<%@ page import="jakarta.servlet.*, java.sql.*, com.project.DBConnection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Kedai Kerepek Maksu</title>
</head>
<body>
    <h1>Welcome to Kedai Kerepek Maksu</h1>
    <nav>
        <ul>
            <li><a href="Dashboard.jsp">Dashboard</a></li>
            <li><a href="ViewProduct.jsp">Inventories</a></li>
            <li><a href="Reports.jsp">Reports</a></li>
            <li><a href="ProfileAccount.jsp">Profile Account</a></li>
            <li><a href="Logout.jsp">Log Out</a></li>
        </ul>
    </nav>

    <!-- Summary Section -->
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

    <section>
        <h2>Summary</h2>
        <p>Total Sales: <%= String.format("%.2f", totalSales) %> USD</p>
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
                <div>
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
</body>
</html>