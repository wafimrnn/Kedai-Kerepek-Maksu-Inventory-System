package com.dao;

import com.model.Drink;
import com.model.Food;
import com.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

	private Connection getConnection() throws SQLException {
        String connectionString = System.getenv("AZURE_SQL_CONNECTIONSTRING");
        if (connectionString == null || connectionString.isEmpty()) {
            throw new SQLException("Connection string is missing!");
        }
        return DriverManager.getConnection(connectionString);
    }

    public boolean addProduct(Product product) throws SQLException {
        String insertProductQuery = "INSERT INTO PRODUCTS (PROD_NAME, PROD_PRICE, QUANTITY_STOCK, RESTOCK_LEVEL, EXPIRY_DATE, PRODUCT_STATUS) VALUES (?, ?, ?, ?, ?, ?)";
        String insertFoodQuery = "INSERT INTO FOOD (FOOD_ID, WEIGHT, PACKAGING_TYPE) VALUES (?, ?, ?)";
        String insertDrinkQuery = "INSERT INTO DRINK (DRINK_ID, VOLUME) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement productStmt = conn.prepareStatement(insertProductQuery, Statement.RETURN_GENERATED_KEYS)) {

            // Insert into PRODUCTS table
            productStmt.setString(1, product.getProductName());
            productStmt.setDouble(2, product.getPrice());
            productStmt.setInt(3, product.getQuantityStock());
            productStmt.setInt(4, product.getRestockLevel());
            productStmt.setDate(5, product.getExpiryDate());
            productStmt.setString(6, product.getProductStatus());
            productStmt.executeUpdate();

            // Get the generated product ID
            try (ResultSet rs = productStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int productId = rs.getInt(1);

                    // Insert into FOOD or DRINK table based on the product type
                    if (product instanceof Food) {
                        Food food = (Food) product;
                        try (PreparedStatement foodStmt = conn.prepareStatement(insertFoodQuery)) {
                            foodStmt.setInt(1, productId);
                            foodStmt.setDouble(2, food.getWeight());
                            foodStmt.setString(3, food.getPackagingType());
                            foodStmt.executeUpdate();
                        }
                    } else if (product instanceof Drink) {
                        Drink drink = (Drink) product;
                        try (PreparedStatement drinkStmt = conn.prepareStatement(insertDrinkQuery)) {
                            drinkStmt.setInt(1, productId);
                            drinkStmt.setDouble(2, drink.getVolume());
                            drinkStmt.executeUpdate();
                        }
                    }
                }
            }
            return true;
        }
    }

    // Retrieve ALL products
    public List<Product> getAllProducts() throws SQLException {
        String productQuery = "SELECT * FROM PRODUCTS WHERE PRODUCT_STATUS = 'ACTIVE'";
        String foodQuery = "SELECT * FROM FOOD WHERE FOOD_ID = ?";
        String drinkQuery = "SELECT * FROM DRINK WHERE DRINK_ID = ?";

        List<Product> products = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement productStmt = conn.prepareStatement(productQuery);
             ResultSet productRs = productStmt.executeQuery()) {

            while (productRs.next()) {
                int productId = productRs.getInt("PROD_ID");
                String category = determineCategory(productId, conn); // Helper function

                if ("FOOD".equals(category)) {
                    try (PreparedStatement foodStmt = conn.prepareStatement(foodQuery)) {
                        foodStmt.setInt(1, productId);
                        try (ResultSet foodRs = foodStmt.executeQuery()) {
                            if (foodRs.next()) {
                                Food food = new Food(productId, category, productId, productId, null, productId, category, category, productId, category);
                                food.setProductId(productId);
                                food.setProductName(productRs.getString("PROD_NAME"));
                                food.setPrice(productRs.getDouble("PROD_PRICE"));
                                food.setQuantityStock(productRs.getInt("QUANTITY_STOCK"));
                                food.setRestockLevel(productRs.getInt("RESTOCK_LEVEL"));
                                String expiryDateStr = productRs.getString("EXPIRY_DATE");
                                if (expiryDateStr != null) {
                                    Date expiryDate = java.sql.Date.valueOf(expiryDateStr); // Convert String to Date
                                    food.setExpiryDate(expiryDate); // Set the Date in the food object
                                }
                                food.setProductStatus(productRs.getString("PRODUCT_STATUS"));
                                food.setWeight(foodRs.getDouble("WEIGHT"));
                                food.setPackagingType(foodRs.getString("PACKAGING_TYPE"));
                                products.add(food);
                            }
                        }
                    }
                } else if ("DRINK".equals(category)) {
                    try (PreparedStatement drinkStmt = conn.prepareStatement(drinkQuery)) {
                        drinkStmt.setInt(1, productId);
                        try (ResultSet drinkRs = drinkStmt.executeQuery()) {
                            if (drinkRs.next()) {
                                Drink drink = new Drink(productId, category, productId, productId, null, productId, category, category, productId);
                                drink.setProductId(productId);
                                drink.setProductName(productRs.getString("PROD_NAME"));
                                drink.setPrice(productRs.getDouble("PROD_PRICE"));
                                drink.setQuantityStock(productRs.getInt("QUANTITY_STOCK"));
                                drink.setRestockLevel(productRs.getInt("RESTOCK_LEVEL"));
                                String expiryDateStr = productRs.getString("EXPIRY_DATE");
                                if (expiryDateStr != null) {
                                    Date expiryDate = java.sql.Date.valueOf(expiryDateStr); // Convert String to Date
                                    drink.setExpiryDate(expiryDate); // Set the Date in the drink object
                                }
                                drink.setProductStatus(productRs.getString("PRODUCT_STATUS"));
                                drink.setVolume(drinkRs.getDouble("VOLUME"));
                                products.add(drink);
                            }
                        }
                    }
                }
            }
        }
        return products;
    }

    private String determineCategory(int productId, Connection conn) throws SQLException {
        String queryFood = "SELECT 1 FROM FOOD WHERE FOOD_ID = ?";
        String queryDrink = "SELECT 1 FROM DRINK WHERE DRINK_ID = ?";
        
        try (PreparedStatement foodStmt = conn.prepareStatement(queryFood)) {
            foodStmt.setInt(1, productId);
            try (ResultSet foodRs = foodStmt.executeQuery()) {
                if (foodRs.next()) {
                    return "FOOD";
                }
            }
        }

        try (PreparedStatement drinkStmt = conn.prepareStatement(queryDrink)) {
            drinkStmt.setInt(1, productId);
            try (ResultSet drinkRs = drinkStmt.executeQuery()) {
                if (drinkRs.next()) {
                    return "DRINK";
                }
            }
        }

        return null; // This case should be handled, as product should belong to either FOOD or DRINK
    }

    // Update product
    public boolean updateProduct(Product product) throws SQLException {
        String updateProductQuery = "UPDATE PRODUCTS SET PROD_NAME = ?, PROD_PRICE = ?, QUANTITY_STOCK = ?, RESTOCK_LEVEL = ?, EXPIRY_DATE = ?, PRODUCT_STATUS = ? WHERE PROD_ID = ?";
        String updateFoodQuery = "UPDATE FOOD SET WEIGHT = ?, PACKAGING_TYPE = ? WHERE FOOD_ID = ?";
        String updateDrinkQuery = "UPDATE DRINK SET VOLUME = ? WHERE DRINK_ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement productStmt = conn.prepareStatement(updateProductQuery)) {

            // Update PRODUCTS table
            productStmt.setString(1, product.getProductName());
            productStmt.setDouble(2, product.getPrice());
            productStmt.setInt(3, product.getQuantityStock());
            productStmt.setInt(4, product.getRestockLevel());
            productStmt.setDate(5, product.getExpiryDate());
            productStmt.setString(6, product.getProductStatus());
            productStmt.setInt(7, product.getProductId());
            productStmt.executeUpdate();

            // Update FOOD or DRINK table based on product type
            if (product instanceof Food) {
                Food food = (Food) product;
                try (PreparedStatement foodStmt = conn.prepareStatement(updateFoodQuery)) {
                    foodStmt.setDouble(1, food.getWeight());
                    foodStmt.setString(2, food.getPackagingType());
                    foodStmt.setInt(3, food.getProductId());
                    foodStmt.executeUpdate();
                }
            } else if (product instanceof Drink) {
                Drink drink = (Drink) product;
                try (PreparedStatement drinkStmt = conn.prepareStatement(updateDrinkQuery)) {
                    drinkStmt.setDouble(1, drink.getVolume());
                    drinkStmt.setInt(2, drink.getProductId());
                    drinkStmt.executeUpdate();
                }
            }
            return true;
        }
    }

 // Delete product by ID
    public boolean deleteProductById(int productId, String productType) throws SQLException {
        String deleteQuery = null;

        // Determine the correct query based on product type
        if ("food".equalsIgnoreCase(productType)) {
            deleteQuery = "DELETE FROM FOOD WHERE FOOD_ID = ?";
        } else if ("drink".equalsIgnoreCase(productType)) {
            deleteQuery = "DELETE FROM DRINK WHERE DRINK_ID = ?";
        }

        if (deleteQuery == null) {
            throw new IllegalArgumentException("Invalid product type");
        }

        // Execute the delete operation
        try (Connection conn = getConnection();  // Updated to use getConnection()
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
            stmt.setInt(1, productId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Return true if a row was deleted
        }
    }
}