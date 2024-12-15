package com.controller;

import com.model.Food;
import com.model.Drink;
import com.model.Product;
import com.manager.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ViewProductServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Starting ViewProductServlet...");

        // Step 1: Fetch product list
        List<Product> products = fetchProductsFromDatabase();

        // Step 2: Add products to request scope
        request.setAttribute("products", products);

        // Step 3: Forward to JSP page
        request.getRequestDispatcher("ViewProduct.jsp").forward(request, response);
    }

    private List<Product> fetchProductsFromDatabase() {
        List<Product> products = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            // Query to fetch active products and their specific child data (Food/Drink)
            String sql = "SELECT p.PROD_ID, p.PROD_NAME, p.PROD_PRICE, p.QUANTITY_STOCK, p.IMAGE_PATH, " +
                         "       f.PACKAGING_TYPE, f.WEIGHT, " +
                         "       d.VOLUME " +
                         "FROM Products p " +
                         "LEFT JOIN Food f ON p.PROD_ID = f.PROD_ID " +
                         "LEFT JOIN Drink d ON p.PROD_ID = d.PROD_ID " +
                         "WHERE p.PROD_STATUS = 'ACTIVE'";

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    // Check if the product is Food or Drink based on which columns are non-null
                    if (rs.getString("PACKAGING_TYPE") != null || rs.getDouble("WEIGHT") > 0) {
                        Food food = new Food();
                        food.setProdId(rs.getInt("PROD_ID"));
                        food.setProdName(rs.getString("PROD_NAME"));
                        food.setProdPrice(rs.getDouble("PROD_PRICE"));
                        food.setQuantityStock(rs.getInt("QUANTITY_STOCK"));
                        food.setImagePath(rs.getString("IMAGE_PATH"));
                        food.setPackagingType(rs.getString("PACKAGING_TYPE"));
                        food.setWeight(rs.getDouble("WEIGHT"));
                        products.add(food);
                    } else if (rs.getDouble("VOLUME") > 0) {
                        Drink drink = new Drink();
                        drink.setProdId(rs.getInt("PROD_ID"));
                        drink.setProdName(rs.getString("PROD_NAME"));
                        drink.setProdPrice(rs.getDouble("PROD_PRICE"));
                        drink.setQuantityStock(rs.getInt("QUANTITY_STOCK"));
                        drink.setImagePath(rs.getString("IMAGE_PATH"));
                        drink.setVolume(rs.getDouble("VOLUME"));
                        products.add(drink);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }
}