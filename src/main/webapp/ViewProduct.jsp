<%@ page import="java.util.List" %>
<%@ page import="com.model.Product" %>
<%@ page import="com.model.Food" %>
<%@ page import="com.model.Drink" %>
<!DOCTYPE html>
<html>
<head>
    <title>View Products</title>
    <style>
        body { font-family: Arial, sans-serif; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .button { padding: 5px 10px; margin-right: 5px; text-decoration: none; }
        .add-button { background-color: green; color: white; }
        .update-button { background-color: orange; color: white; }
        .delete-button { background-color: red; color: white; }
    </style>
</head>
<body>
    <h2>Product List</h2>
    <a href="CreateProduct.html" class="button add-button">Add Product</a>
    <table>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Restock Level</th>
            <th>Expiry Date</th>
            <th>Image</th>
            <th>Category</th>
            <th>Details</th>
            <th>Actions</th>
        </tr>
        <%
            List<Product> productList = (List<Product>) request.getAttribute("productList");
            if (productList != null) {
                for (Product p : productList) {
        %>
        <tr>
            <td><%= p.getProdId() %></td>
            <td><%= p.getProdName() %></td>
            <td><%= p.getProdPrice() %></td>
            <td><%= p.getQuantityStock() %></td>
            <td><%= p.getRestockLevel() %></td>
            <td><%= p.getExpiryDate() %></td>
            <td>
                <% if (p.getImagePath() != null) { %>
                    <img src="<%= p.getImagePath() %>" alt="Product Image" width="50" height="50"/>
                <% } else { %>
                    N/A
                <% } %>
            </td>
            <td><%= p.getProdStatus() %></td>
            <td>
                <% 
                    if ("Food".equalsIgnoreCase(p.getProdStatus())) {
                        Food food = (Food) p;
                %>
                    Packaging: <%= food.getPackagingType() %><br/>
                    Weight: <%= food.getWeight() %> kg
                <% 
                    } else if ("Drink".equalsIgnoreCase(p.getProdStatus())) {
                        Drink drink = (Drink) p;
                %>
                    Volume: <%= drink.getVolume() %> L
                <% } else { %>
                    N/A
                <% } %>
            </td>
            <td>
                <form action="UpdateProduct.jsp" method="get" style="display:inline;">
                    <input type="hidden" name="prodId" value="<%= p.getProdId() %>"/>
                    <input type="submit" value="Update" class="button update-button"/>
                </form>
                <form action="DeleteProductServlet" method="post" style="display:inline;" 
                      onsubmit="return confirm('Are you sure you want to delete this product?');">
                    <input type="hidden" name="prodId" value="<%= p.getProdId() %>"/>
                    <input type="submit" value="Delete" class="button delete-button"/>
                </form>
            </td>
        </tr>
        <%
                }
            }
        %>
    </table>
</body>
</html>