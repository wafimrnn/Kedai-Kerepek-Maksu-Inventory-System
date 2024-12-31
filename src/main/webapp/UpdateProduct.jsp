<%@ page import="com.model.Product, com.model.Food, com.model.Drink" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Product</title>
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
            background-color: #f4f4f9;
        }

        /* Sidebar */
        .sidebar {
            width: 220px;
            background-color: #343a40;
            color: white;
            padding: 20px;
        }

        .sidebar h2 {
            text-align: center;
            border-bottom: 1px solid #495057;
            padding-bottom: 10px;
        }

        .sidebar a {
            display: block;
            color: white;
            text-decoration: none;
            padding: 10px;
            border-radius: 4px;
            transition: 0.3s;
        }

        .sidebar a.active, .sidebar a:hover {
            background-color: #007BFF;
        }

        .main-content {
            flex: 1;
            padding: 20px;
        }

        .form-container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .form-group {
            margin-bottom: 15px;
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
        }
    </style>
    <script>
        function toggleCategoryFields() {
            const category = document.getElementById("prodStatus").value;
            document.getElementById("foodFields").style.display = category === "Food" ? "block" : "none";
            document.getElementById("drinkFields").style.display = category === "Drink" ? "block" : "none";
        }

        window.onload = toggleCategoryFields;
    </script>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Dashboard</h2>
        <a href="dashboard.html">Dashboard</a>
        <a href="ViewProductServlet" class="active">Product</a>
        <a href="#">Sales</a>
        <a href="#">Report</a>
        <a href="#">Account</a>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <h2>Update Product</h2>
        <div class="form-container">
            <%
                Product product = (Product) request.getAttribute("product");
                if (product == null) {
            %>
                <p style="color:red;">Product not found.</p>
            <% } else { %>
            <form action="UpdateProductServlet" method="post">
                <input type="hidden" name="prodId" value="<%= product.getProdId() %>">

                <div class="form-group">
                    <label for="prodName">Product Name:</label>
                    <input type="text" id="prodName" name="prodName" value="<%= product.getProdName() %>" required>
                </div>

                <div class="form-group">
                    <label for="prodPrice">Price:</label>
                    <input type="number" id="prodPrice" name="prodPrice" step="0.01" value="<%= product.getProdPrice() %>" required>
                </div>

                <div class="form-group">
                    <label for="quantityStock">Stock:</label>
                    <input type="number" id="quantityStock" name="quantityStock" value="<%= product.getQuantityStock() %>" required>
                </div>

                <div class="form-group">
                    <label for="prodStatus">Category:</label>
                    <select id="prodStatus" name="prodStatus" onchange="toggleCategoryFields()">
                        <option value="Food" <%= "Food".equals(product.getProdStatus()) ? "selected" : "" %>>Food</option>
                        <option value="Drink" <%= "Drink".equals(product.getProdStatus()) ? "selected" : "" %>>Drink</option>
                    </select>
                </div>

                <div id="foodFields" style="display:none;">
                    <div class="form-group">
                        <label for="packagingType">Packaging Type:</label>
                        <input type="text" id="packagingType" name="packagingType" value="<%= product instanceof Food ? ((Food) product).getPackagingType() : "" %>">
                    </div>
                    <div class="form-group">
                        <label for="weight">Weight (kg):</label>
                        <input type="number" id="weight" name="weight" step="0.01" value="<%= product instanceof Food ? ((Food) product).getWeight() : "" %>">
                    </div>
                </div>

                <div id="drinkFields" style="display:none;">
                    <div class="form-group">
                        <label for="volume">Volume (L):</label>
                        <input type="number" id="volume" name="volume" step="0.01" value="<%= product instanceof Drink ? ((Drink) product).getVolume() : "" %>">
                    </div>
                </div>
                <div class="form-group">
			        <label for="image">Product Image:</label>
			        <input type="file" id="image" name="image">
			        <small>Upload a new image or leave blank to keep the current image.</small>
    			</div>

                <button type="submit">Update Product</button>
                <button type="button" class="cancel-button" onclick="window.location.href='ViewProductServlet'">Cancel</button>
            </form>
            <% } %>
        </div>
    </div>
</body>
</html>