package com.dao;

import com.model.Drink;
import com.model.Food;
import com.model.Product;
import com.manager.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

	// Get all active products
    public List<Product> getAllActiveProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.PROD_ID, p.PROD_NAME, p.PROD_PRICE, p.QUANTITY_STOCK, p.IMAGE_PATH, " +
                     "       f.PACKAGING_TYPE, f.WEIGHT, d.VOLUME " +
                     "FROM PRODUCTS p " +
                     "LEFT JOIN FOOD f ON p.PROD_ID = f.PROD_ID " +
                     "LEFT JOIN DRINK d ON p.PROD_ID = d.PROD_ID " +
                     "WHERE p.PROD_STATUS = 'ACTIVE'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String imagePath = rs.getString("IMAGE_PATH");
                if (imagePath == null || imagePath.isEmpty()) {
                    imagePath = "img/default-image.jpg";
                }

                if (rs.getString("PACKAGING_TYPE") != null) {
                    Food food = new Food();
                    food.setProdId(rs.getInt("PROD_ID"));
                    food.setProdName(rs.getString("PROD_NAME"));
                    food.setProdPrice(rs.getDouble("PROD_PRICE"));
                    food.setQuantityStock(rs.getInt("QUANTITY_STOCK"));
                    food.setImagePath(imagePath);
                    food.setPackagingType(rs.getString("PACKAGING_TYPE"));
                    food.setWeight(rs.getDouble("WEIGHT"));
                    products.add(food);
                } else if (rs.getDouble("VOLUME") > 0) {
                    Drink drink = new Drink();
                    drink.setProdId(rs.getInt("PROD_ID"));
                    drink.setProdName(rs.getString("PROD_NAME"));
                    drink.setProdPrice(rs.getDouble("PROD_PRICE"));
                    drink.setQuantityStock(rs.getInt("QUANTITY_STOCK"));
                    drink.setImagePath(imagePath);
                    drink.setVolume(rs.getDouble("VOLUME"));
                    products.add(drink);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
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
    public static void updateProduct(Product product) throws Exception {
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
    public boolean updateProductStatus(Product product) {
        String query = "UPDATE Products SET PROD_STATUS = ? WHERE PROD_ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getProdStatus());
            statement.setInt(2, product.getProdId());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
