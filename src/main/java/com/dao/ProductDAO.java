package com.dao;

import com.model.Drink;
import com.model.Food;
import com.model.Product;
import com.manager.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Get all products (both active and inactive)
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.PROD_ID, p.PROD_NAME, p.PROD_PRICE, p.QUANTITY_STOCK, p.IMAGE_PATH, p.PROD_STATUS, " +
                     "       f.PACKAGING_TYPE, f.WEIGHT, d.VOLUME " +
                     "FROM PRODUCTS p " +
                     "LEFT JOIN FOOD f ON p.PROD_ID = f.PROD_ID " +
                     "LEFT JOIN DRINK d ON p.PROD_ID = d.PROD_ID";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    // Get only active products
    public List<Product> getAllActiveProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.PROD_ID, p.PROD_NAME, p.PROD_PRICE, p.QUANTITY_STOCK, p.IMAGE_PATH, p.PROD_STATUS, " +
                     "       f.PACKAGING_TYPE, f.WEIGHT, d.VOLUME " +
                     "FROM PRODUCTS p " +
                     "LEFT JOIN FOOD f ON p.PROD_ID = f.PROD_ID " +
                     "LEFT JOIN DRINK d ON p.PROD_ID = d.PROD_ID " +
                     "WHERE p.PROD_STATUS = 'ACTIVE'";  // <-- Only active products

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    // Helper method to map result set to Product/Food/Drink
    private Product mapProduct(ResultSet rs) throws SQLException {
        String imagePath = rs.getString("IMAGE_PATH");
        if (imagePath == null || imagePath.isEmpty()) {
            imagePath = "img/default-image.jpg";
        }

        Product product;
        if (rs.getString("PACKAGING_TYPE") != null) {
            product = new Food();
            ((Food) product).setPackagingType(rs.getString("PACKAGING_TYPE"));
            ((Food) product).setWeight(rs.getDouble("WEIGHT"));
        } else if (rs.getDouble("VOLUME") > 0) {
            product = new Drink();
            ((Drink) product).setVolume(rs.getDouble("VOLUME"));
        } else {
            product = new Product();
        }

        product.setProdId(rs.getInt("PROD_ID"));
        product.setProdName(rs.getString("PROD_NAME"));
        product.setProdPrice(rs.getDouble("PROD_PRICE"));
        product.setQuantityStock(rs.getInt("QUANTITY_STOCK"));
        product.setProdStatus(rs.getString("PROD_STATUS"));
        product.setImagePath(imagePath);

        return product;
    }

    // Insert product (Azure SQL)
    public int insertProduct(Product product) throws SQLException {
        int prodId = -1;

        String sql = "INSERT INTO PRODUCTS (PROD_NAME, PROD_PRICE, QUANTITY_STOCK, RESTOCK_LEVEL, EXPIRY_DATE, IMAGE_PATH, PROD_STATUS) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?); " +
                     "SELECT SCOPE_IDENTITY();";  // Get the last inserted ID

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, product.getProdName());
            ps.setDouble(2, product.getProdPrice());
            ps.setInt(3, product.getQuantityStock());
            ps.setInt(4, product.getRestockLevel());

            if (product.getExpiryDate() != null) {
                ps.setDate(5, new java.sql.Date(product.getExpiryDate().getTime()));
            } else {
                ps.setNull(5, java.sql.Types.DATE);
            }

            ps.setString(6, product.getImagePath());
            ps.setString(7, "ACTIVE");

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    prodId = generatedKeys.getInt(1);
                }
            }
        }

        if (product instanceof Food) {
            Food food = (Food) product;
            String foodSql = "INSERT INTO FOOD (PROD_ID, PACKAGING_TYPE, WEIGHT) VALUES (?, ?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement psFood = conn.prepareStatement(foodSql)) {
                psFood.setInt(1, prodId);
                psFood.setString(2, food.getPackagingType());
                psFood.setDouble(3, food.getWeight());
                psFood.executeUpdate();
            }
        } else if (product instanceof Drink) {
            Drink drink = (Drink) product;
            String drinkSql = "INSERT INTO DRINK (PROD_ID, VOLUME) VALUES (?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement psDrink = conn.prepareStatement(drinkSql)) {
                psDrink.setInt(1, prodId);
                psDrink.setDouble(2, drink.getVolume());
                psDrink.executeUpdate();
            }
        }

        return prodId;
    }

    // Update product details
    public void updateProduct(Product product) throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            String updateSQL = "UPDATE Products SET PROD_NAME = ?, PROD_PRICE = ?, QUANTITY_STOCK = ?, IMAGE_PATH = ? WHERE PROD_ID = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSQL)) {
                ps.setString(1, product.getProdName());
                ps.setDouble(2, product.getProdPrice());
                ps.setInt(3, product.getQuantityStock());
                ps.setString(4, product.getImagePath());
                ps.setInt(5, product.getProdId());
                ps.executeUpdate();
            }

            if (product instanceof Food) {
                Food food = (Food) product;
                String updateFoodSQL = "UPDATE Foods SET PACKAGING_TYPE = ?, WEIGHT = ? WHERE PROD_ID = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateFoodSQL)) {
                    ps.setString(1, food.getPackagingType());
                    ps.setDouble(2, food.getWeight());
                    ps.setInt(3, food.getProdId());
                    ps.executeUpdate();
                }
            } else if (product instanceof Drink) {
                Drink drink = (Drink) product;
                String updateDrinkSQL = "UPDATE Drinks SET VOLUME = ? WHERE PROD_ID = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateDrinkSQL)) {
                    ps.setDouble(1, drink.getVolume());
                    ps.setInt(2, drink.getProdId());
                    ps.executeUpdate();
                }
            }
        }
    }

    public Product getProductById(int prodId) {
        String query = "SELECT * FROM Products WHERE PROD_ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, prodId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Product product = new Product();
                product.setProdId(resultSet.getInt("PROD_ID"));
                product.setProdName(resultSet.getString("PROD_NAME"));
                product.setProdPrice(resultSet.getDouble("PROD_PRICE"));
                product.setQuantityStock(resultSet.getInt("QUANTITY_STOCK"));
                product.setProdStatus(resultSet.getString("PROD_STATUS"));
                product.setImagePath(resultSet.getString("IMAGE_PATH"));
                return product;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update product status
    public boolean updateProductStatus(int prodId, String newStatus) {
        String sql = "UPDATE PRODUCTS SET PROD_STATUS = ? WHERE PROD_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, prodId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;  // Returns true if update was successful

        } catch (SQLException e) {
            System.err.println("Error updating product status for ID: " + prodId);
            e.printStackTrace();
            return false;  // Return false if update fails
        }
    }

    // Get inventory report for all products (both active and inactive)
    public List<Product> getInventoryReport() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.PROD_ID, p.PROD_NAME, p.PROD_PRICE, p.QUANTITY_STOCK, p.RESTOCK_LEVEL, p.EXPIRY_DATE, p.IMAGE_PATH, p.PROD_STATUS, " +
                     "       f.PACKAGING_TYPE, f.WEIGHT, d.VOLUME " +
                     "FROM PRODUCTS p " +
                     "LEFT JOIN FOOD f ON p.PROD_ID = f.PROD_ID " +
                     "LEFT JOIN DRINK d ON p.PROD_ID = d.PROD_ID";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(mapProduct(rs));  // Map each row to a Product object
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }
}
