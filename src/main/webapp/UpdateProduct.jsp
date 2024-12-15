<%@ page import="com.manager.DBConnection" %>
<%@ page import="com.model.Product" %>
<%@ page import="com.model.Food" %>
<%@ page import="com.model.Drink" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Update Product</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .form-container { width: 50%; margin: auto; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; }
        input, select { width: 100%; padding: 8px; }
        .submit-button { background-color: orange; color: white; padding: 10px 15px; border: none; cursor: pointer; }
    </style>
    <script>
        function toggleCategoryFields() {
            var category = document.getElementById("prodStatus").value;
            if (category === "Food") {
                document.getElementById("foodFields").style.display = "block";
                document.getElementById("drinkFields").style.display = "none";
            } else if (category === "Drink") {
                document.getElementById("foodFields").style.display = "none";
                document.getElementById("drinkFields").style.display = "block";
            } else {
                document.getElementById("foodFields").style.display = "none";
                document.getElementById("drinkFields").style.display = "none";
            }
        }
    </script>
</head>
<body>
    <div class="form-container">
        <h2>Update Product</h2>
        <%
            String prodIdParam = request.getParameter("prodId");
            if (prodIdParam == null) {
        %>
            <p>Product ID is missing.</p>
        <%
            } else {
                int prodId = Integer.parseInt(prodIdParam);
                String query = "SELECT p.*, f.PACKAGING_TYPE, f.WEIGHT, d.VOLUME " +
                               "FROM Products p " +
                               "LEFT JOIN Food f ON p.PROD_ID = f.PROD_ID " +
                               "LEFT JOIN Drink d ON p.PROD_ID = d.PROD_ID " +
                               "WHERE p.PROD_ID = ?";

                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, prodId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String prodName = rs.getString("PROD_NAME");
                            double prodPrice = rs.getDouble("PROD_PRICE");
                            int quantityStock = rs.getInt("QUANTITY_STOCK");
                            int restockLevel = rs.getInt("RESTOCK_LEVEL");
                            Date expiryDate = rs.getDate("EXPIRY_DATE");
                            String imagePath = rs.getString("IMAGE_PATH");
                            String prodStatus = rs.getString("PROD_STATUS");
                            String packagingType = rs.getString("PACKAGING_TYPE");
                            Double weight = rs.getDouble("WEIGHT");
                            if (rs.wasNull()) weight = null;
                            Double volume = rs.getDouble("VOLUME");
                            if (rs.wasNull()) volume = null;
        %>
        <form action="UpdateProductServlet" method="post">
            <input type="hidden" name="prodId" value="<%= prodId %>"/>
            <div class="form-group">
                <label for="prodName">Product Name:</label>
                <input type="text" id="prodName" name="prodName" value="<%= prodName %>" required/>
            </div>
            <div class="form-group">
                <label for="prodPrice">Price:</label>
                <input type="number" step="0.01" id="prodPrice" name="prodPrice" value="<%= prodPrice %>" required/>
            </div>
            <div class="form-group">
                <label for="quantityStock">Quantity in Stock:</label>
                <input type="number" id="quantityStock" name="quantityStock" value="<%= quantityStock %>" required/>
            </div>
            <div class="form-group">
                <label for="restockLevel">Restock Level:</label>
                <input type="number" id="restockLevel" name="restockLevel" value="<%= restockLevel %>" required/>
            </div>
            <div class="form-group">
                <label for="expiryDate">Expiry Date:</label>
                <input type="date" id="expiryDate" name="expiryDate" value="<%= expiryDate %>" required/>
            </div>
            <div class="form-group">
                <label for="imagePath">Image URL:</label>
                <input type="text" id="imagePath" name="imagePath" value="<%= imagePath != null ? imagePath : "" %>"/>
            </div>
            <div class="form-group">
                <label for="prodStatus">Category:</label>
                <select id="prodStatus" name="prodStatus" onchange="toggleCategoryFields()" required>
                    <option value="">Select Category</option>
                    <option value="Food" <%= "Food".equalsIgnoreCase(prodStatus) ? "selected" : "" %>>Food</option>
                    <option value="Drink" <%= "Drink".equalsIgnoreCase(prodStatus) ? "selected" : "" %>>Drink</option>
                </select>
            </div>
            <div id="foodFields" style="display:<%= "Food".equalsIgnoreCase(prodStatus) ? "block" : "none" %>;">
                <div class="form-group">
                    <label for="packagingType">Packaging Type:</label>
                    <input type="text" id="packagingType" name="packagingType" value="<%= packagingType != null ? packagingType : "" %>"/>
                </div>
                <div class="form-group">
                    <label for="weight">Weight (kg):</label>
                    <input type="number" step="0.01" id="weight" name="weight" value="<%= weight != null ? weight : "" %>"/>
                </div>
            </div>
            <div id="drinkFields" style="display:<%= "Drink".equalsIgnoreCase(prodStatus) ? "block" : "none" %>;">
                <div class="form-group">
                    <label for="volume">Volume (L):</label>
                    <input type="number" step="0.01" id="volume" name="volume" value="<%= volume != null ? volume : "" %>"/>
                </div>
            </div>
            <button type="submit" class="submit-button">Update Product</button>
        </form>
        <script>
            // Initialize category fields on page load
            window.onload = function() {
                toggleCategoryFields();
            };
        </script>
        <%
                        } else {
        %>
            <p>Product not found.</p>
        <%
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
        %>
            <p>Error retrieving product details.</p>
        <%
                }
            }
        %>
    </div>
</body>
</html>