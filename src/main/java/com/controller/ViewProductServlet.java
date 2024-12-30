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
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    System.out.println("Starting ViewProductServlet...");

	    // Fetch products
	    List<Product> products = fetchProductsFromDatabase();

	    // Debug: Log product size
	    System.out.println("Fetched products: " + (products != null ? products.size() : 0));

	    // Set products in request scope
	    request.setAttribute("products", products);

	    // Disable caching to prevent stale data issues
	    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    response.setHeader("Pragma", "no-cache");
	    response.setDateHeader("Expires", 0);

	    // Forward to JSP
	    request.getRequestDispatcher("ViewProduct.jsp").forward(request, response);
	}

	private List<Product> fetchProductsFromDatabase() {
	    List<Product> products = new ArrayList<>();

	    try (Connection conn = DBConnection.getConnection()) {
	        String sql = "SELECT p.PROD_ID, p.PROD_NAME, p.PROD_PRICE, p.QUANTITY_STOCK, p.IMAGE_PATH, " +
	                     "       f.PACKAGING_TYPE, f.WEIGHT, d.VOLUME " +
	                     "FROM Products p " +
	                     "LEFT JOIN Food f ON p.PROD_ID = f.PROD_ID " +
	                     "LEFT JOIN Drink d ON p.PROD_ID = d.PROD_ID " +
	                     "WHERE p.PROD_STATUS = 'ACTIVE'";

	        try (PreparedStatement ps = conn.prepareStatement(sql);
	             ResultSet rs = ps.executeQuery()) {

	            while (rs.next()) {
	                System.out.println("Database Row: PROD_ID=" + rs.getInt("PROD_ID") +
	                                   ", PROD_NAME=" + rs.getString("PROD_NAME") +
	                                   ", PROD_PRICE=" + rs.getDouble("PROD_PRICE") +
	                                   ", QUANTITY_STOCK=" + rs.getInt("QUANTITY_STOCK") +
	                                   ", IMAGE_PATH=" + rs.getString("IMAGE_PATH"));

	                if (rs.getString("PACKAGING_TYPE") != null) {
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
