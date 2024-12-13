<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Product</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }

        .form-container {
            background-color: white;
            border-radius: 10px;
            padding: 25px;
            max-width: 600px;
            margin: 40px auto;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .form-container h3 {
            text-align: center;
            font-size: 1.6em;
            margin-bottom: 25px;
            color: #333;
        }

        .form-group {
            margin-bottom: 20px;
            display: flex;
            flex-direction: column;
        }

        .form-group label {
            margin-bottom: 8px;
            font-weight: bold;
            font-size: 1.1em;
            color: #444;
        }

        .form-group input, .form-group select {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 1em;
            background-color: #fafafa;
            transition: border-color 0.3s ease;
        }

        .form-group input:focus, .form-group select:focus {
            outline: none;
            border-color: #ffc107;
        }

        .form-actions {
            text-align: center;
        }

        .form-actions button {
            padding: 12px 25px;
            background-color: #000;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: bold;
            text-transform: uppercase;
            transition: background-color 0.3s ease;
        }

        .form-actions button:hover {
            background-color: #e0a800;
        }
    </style>
</head>
<body>

    <div class="form-container">
        <h3>Update Product</h3>
        <form action="UpdateProductServlet" method="post">
            <div class="form-group">
                <label for="product-name">Product Name</label>
                <input type="text" id="product-name" name="product-name" value="${product.prodName}" placeholder="Kerepek Bawang" required>
            </div>
            <div class="form-group">
                <label for="category">Category</label>
                <select id="category" name="category" required>
                    <option value="snacks" <c:if test="${product.category == 'snacks'}">selected</c:if>>Foods</option>
                    <option value="drinks" <c:if test="${product.category == 'drinks'}">selected</c:if>>Drinks</option>
                </select>
            </div>
            <div class="form-group">
                <label for="product-id">Product ID</label>
                <input type="text" id="product-id" name="product-id" value="${product.prodId}" readonly>
            </div>
            <div class="form-group">
                <label for="quantity">Quantity Stock</label>
                <input type="number" id="quantity" name="quantity" value="${product.quantityStock}" placeholder="3" min="1" required>
            </div>
            <div class="form-group">
                <label for="price">Price (RM)</label>
                <input type="text" id="price" name="price" value="${product.prodPrice}" placeholder="RM12.00" required>
            </div>

            <!-- Food-specific fields -->
            <div class="form-group" id="food-fields" style="display: none;">
                <label for="weight">Weight (g)</label>
                <input type="number" id="weight" name="weight" value="${product.weight}" placeholder="150" min="1">
            </div>
            <div class="form-group" id="food-fields" style="display: none;">
                <label for="packaging-type">Packaging Type</label>
                <input type="text" id="packaging-type" name="packaging-type" value="${product.packagingType}" placeholder="Plastic">
            </div>

            <!-- Drink-specific fields -->
            <div class="form-group" id="drink-fields" style="display: none;">
                <label for="volume">Volume (ml)</label>
                <input type="number" id="volume" name="volume" value="${product.volume}" placeholder="500" min="1">
            </div>

            <div class="form-group">
                <label for="expiry-date">Expiry Date</label>
                <input type="date" id="expiry-date" name="expiry-date" value="${product.expiryDate}" required>
            </div>

            <div class="form-group">
                <label for="restock-level">Restock Level</label>
                <input type="number" id="restock-level" name="restock-level" value="${product.restockLevel}" placeholder="5" min="1">
            </div>
            <div class="form-group">
                <label for="product-status">Product Status</label>
                <select id="product-status" name="product-status" required>
                    <option value="active" <c:if test="${product.prodStatus == 'active'}">selected</c:if>>Active</option>
                    <option value="inactive" <c:if test="${product.prodStatus == 'inactive'}">selected</c:if>>Inactive</option>
                </select>
            </div>

            <div class="form-actions">
                <button type="submit">Update</button>
            </div>
        </form>
    </div>

    <script>
        // Show and hide fields based on category selection
        const categorySelect = document.getElementById('category');
        const foodFields = document.getElementById('food-fields');
        const drinkFields = document.getElementById('drink-fields');

        categorySelect.addEventListener('change', function () {
            if (categorySelect.value === 'snacks') {
                foodFields.style.display = 'block';
                drinkFields.style.display = 'none';
            } else if (categorySelect.value === 'drinks') {
                foodFields.style.display = 'none';
                drinkFields.style.display = 'block';
            }
        });

        // Trigger initial display based on the current category
        if (categorySelect.value === 'snacks') {
            foodFields.style.display = 'block';
            drinkFields.style.display = 'none';
        } else if (categorySelect.value === 'drinks') {
            foodFields.style.display = 'none';
            drinkFields.style.display = 'block';
        }
    </script>

</body>
</html>
