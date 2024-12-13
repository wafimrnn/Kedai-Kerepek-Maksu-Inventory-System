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
            padding: 20px;
            max-width: 600px;
            margin: 30px auto;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .form-container h3 {
            text-align: center;
            font-size: 1.5em;
            margin-bottom: 20px;
        }

        .form-group {
            margin-bottom: 15px;
            display: flex;
            flex-direction: column;
        }

        .form-group label {
            margin-bottom: 5px;
            font-weight: bold;
        }

        .form-group input, .form-group select {
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 1em;
        }

        .form-group input:focus, .form-group select:focus {
            outline: none;
            border-color: #ffc107;
        }

        .form-actions {
            text-align: center;
        }

        .form-actions button {
            padding: 10px 20px;
            background-color: #000;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: bold;
            text-transform: uppercase;
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
                <input type="text" id="product-name" name="product-name" value="${product.productName}" placeholder="Kerepek Bawang">
            </div>
            <div class="form-group">
                <label for="category">Category</label>
                <select id="category" name="category">
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
                <input type="number" id="quantity" name="quantity" value="${product.quantity}" placeholder="3">
            </div>
            <div class="form-group">
                <label for="price">Price</label>
                <input type="text" id="price" name="price" value="${product.price}" placeholder="RM12.00">
            </div>
            <div class="form-actions">
                <button type="submit">Update</button>
            </div>
        </form>
    </div>

</body>
</html>