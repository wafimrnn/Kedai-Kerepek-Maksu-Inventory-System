<!-- DeleteProduct.jsp -->
<html>
<body>
    <h2>Are you sure you want to delete this product?</h2>
    <form action="delete-product" method="post">
        <input type="hidden" name="prodId" value="${prodId}">
        <input type="submit" value="Yes, delete">
        <a href="view-products">No, go back</a>
    </form>
</body>
</html>