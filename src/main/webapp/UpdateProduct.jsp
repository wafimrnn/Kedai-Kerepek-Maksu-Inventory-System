<%@ page import="com.model.Product, com.model.Food, com.model.Drink" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Product</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="css/updateProduct.css">
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
        <a href="ViewProduct.jsps" class="active">Product</a>
        <a href="CreateSales.jsp">Sales</a>
        <a href="Report.html">Report</a>
        <a href="ViewAccount.jsp">Account</a>
    </div>
    <!-- Head Bar -->
	<div class="head-bar">
	    <div class="title">Sales</div>
	    <div class="icons">
	        <i class="fas fa-bell" id="notification-icon" title="Notifications"></i>
	        <i class="fas fa-user-circle" title="Account"></i>
	    </div>
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
            <form action="UpdateProductServlet" method="post" enctype="multipart/form-data">
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
		
		    <!-- Category Field -->
		    <div class="form-group">
		        <label for="category">Category:</label>
		        <select id="category" name="category" onchange="toggleCategoryFields()" required>
		            <option value="Food" <%= product instanceof Food ? "selected" : "" %>>Food</option>
		            <option value="Drink" <%= product instanceof Drink ? "selected" : "" %>>Drink</option>
		        </select>
		    </div>
		
		    <!-- Dynamic Fields for Food -->
		    <div id="foodFields" style="<%= product instanceof Food ? "display:block;" : "display:none;" %>">
		        <div class="form-group">
		            <label for="packagingType">Packaging Type:</label>
		            <input type="text" id="packagingType" name="packagingType" value="<%= product instanceof Food ? ((Food) product).getPackagingType() : "" %>">
		        </div>
		        <div class="form-group">
		            <label for="weight">Weight (kg):</label>
		            <input type="number" id="weight" name="weight" step="0.01" value="<%= product instanceof Food ? ((Food) product).getWeight() : "" %>">
		        </div>
		    </div>
		
		    <!-- Dynamic Fields for Drink -->
		    <div id="drinkFields" style="<%= product instanceof Drink ? "display:block;" : "display:none;" %>">
		        <div class="form-group">
		            <label for="volume">Volume (L):</label>
		            <input type="number" id="volume" name="volume" step="0.01" value="<%= product instanceof Drink ? ((Drink) product).getVolume() : "" %>">
		        </div>
		    </div>
		
		    <!-- Product Status Field -->
		    <div class="form-group">
		        <label for="prodStatus">Status:</label>
		        <select id="prodStatus" name="prodStatus" required>
		            <option value="Active" <%= "Active".equals(product.getProdStatus()) ? "selected" : "" %>>Active</option>
		            <option value="Inactive" <%= "Inactive".equals(product.getProdStatus()) ? "selected" : "" %>>Inactive</option>
		        </select>
		    </div>
		
		    <div class="form-group">
		        <label for="image">Product Image:</label>
		        <input type="file" id="image" name="image">
		        <small>Upload a new image or leave blank to keep the current image.</small>
		    </div>
		
		    <button type="submit">Update Product</button>
		    <button type="button" class="cancel-button" onclick="window.location.href='ViewProductServlet'">Cancel</button>
		</form>
		
		<script>
		    function toggleCategoryFields() {
		        const category = document.getElementById("category").value;
		        document.getElementById("foodFields").style.display = category === "Food" ? "block" : "none";
		        document.getElementById("drinkFields").style.display = category === "Drink" ? "block" : "none";
		    }
		
		    window.onload = toggleCategoryFields;
		</script>
            <% } %>
        </div>
    </div>
    <div id="notification-popup" style="display: none;">
	    <ul id="notification-list"></ul>
	</div>
</body>
</html>