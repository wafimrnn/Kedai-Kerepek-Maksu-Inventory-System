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
            margin: 0;
            padding: 0;
            background-color: #f5f5f5;
        }

        .sidebar {
            width: 250px;
            background-color: #2c2c2c;
            color: white;
            position: fixed;
            height: 100%;
            padding: 20px;
        }

        .sidebar h2 {
            margin: 0;
            font-size: 1.5em;
        }

        .sidebar ul {
            list-style-type: none;
            padding: 0;
            margin-top: 20px;
        }

        .sidebar ul li {
            margin: 10px 0;
        }

        .sidebar ul li a {
            text-decoration: none;
            color: white;
            font-size: 1em;
            display: block;
            padding: 10px;
            border-radius: 5px;
        }

        .sidebar ul li a:hover, .sidebar ul li a.active {
            background-color: #444;
        }

        .content {
            margin-left: 270px;
            padding: 20px;
        }

        .top-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .top-bar button {
            background-color: #ffc107;
            border: none;
            padding: 10px 15px;
            color: white;
            border-radius: 5px;
            cursor: pointer;
        }

        .top-bar button:hover {
            background-color: #e0a800;
        }

        .form-container {
            background-color: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            max-width: 600px;
            margin: 0 auto;
        }

        .form-container h3 {
            margin-bottom: 20px;
            font-size: 1.5em;
            text-align: center;
        }

        .form-group {
            display: flex;
            justify-content: space-between;
            margin-bottom: 15px;
        }

        .form-group label {
            flex: 1;
            margin-right: 10px;
            font-weight: bold;
        }

        .form-group input, .form-group select {
            flex: 2;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 5px;
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
        }

        .form-actions button:hover {
            background-color: #e0a800;
        }
        
		        .black-button {
		    padding: 10px 20px;
		    background-color: #e0a800;
		    color: white;
		    border: none;
		    border-radius: 5px;
		    cursor: pointer;
		    font-weight: bold;
		    text-transform: uppercase;
		}

			.black-button:hover {
			    background-color: #444;
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
                    <option value="snacks" <c:if test="${product.category == 'snacks'}">selected</c:if>>Snacks</option>
                    <option value="drinks" <c:if test="${product.category == 'drinks'}">selected</c:if>>Drinks</option>
                    <option value="others" <c:if test="${product.category == 'others'}">selected</c:if>>Others</option>
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
                <button type="submit" class="black-button">Update</button>
            </div>
        </form>
    </div>
</body>
</html>
