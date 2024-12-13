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
                <input type="text" id="product-name" name="product-name" value="${product.productName}" placeholder="Kerepek Bawang" required>
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
                <input type="text" id="product-id" name="product-id" value="${product.productId}" readonly>
            </div>
            <div class="form-group">
                <label for="quantity">Quantity Stock</label>
                <input type="number" id="quantity" name="quantity" value="${product.quantity}" placeholder="3" min="1" required>
            </div>
            <div class="form-group">
                <label for="price">Price (RM)</label>
                <input type="text" id="price" name="price" value="${product.price}" placeholder="RM12.00" required>
            </div>
            <div class="form-actions">
                <button type="submit">Update</button>
            </div>
        </form>
    </div>

</body>
</html>
