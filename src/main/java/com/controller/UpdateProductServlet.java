package com.controller;

import com.manager.DBConnection;
import com.model.Food;
import com.model.Drink;
import com.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UpdateProductServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int prodId = Integer.parseInt(request.getParameter("prodId"));
            Connection conn = DBConnection.getConnection();
            String query = "SELECT * FROM Products WHERE PROD_ID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, prodId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product product = new Product();
                product.setProdId(prodId);
                product.setProdName(rs.getString("PROD_NAME"));
                product.setProdPrice(rs.getDouble("PROD_PRICE"));
                product.setQuantityStock(rs.getInt("QUANTITY_STOCK"));
                product.setProdStatus(rs.getString("PROD_STATUS"));
                request.setAttribute("product", product);
                request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
            } else {
                response.sendRedirect("ViewProductServlet");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ViewProductServlet");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int prodId = Integer.parseInt(request.getParameter("prodId"));
            String prodName = request.getParameter("prodName");
            double prodPrice = Double.parseDouble(request.getParameter("prodPrice"));
            int quantityStock = Integer.parseInt(request.getParameter("quantityStock"));

            Connection conn = DBConnection.getConnection();
            String updateSQL = "UPDATE Products SET PROD_NAME = ?, PROD_PRICE = ?, QUANTITY_STOCK = ? WHERE PROD_ID = ?";
            PreparedStatement ps = conn.prepareStatement(updateSQL);
            ps.setString(1, prodName);
            ps.setDouble(2, prodPrice);
            ps.setInt(3, quantityStock);
            ps.setInt(4, prodId);
            ps.executeUpdate();

            response.sendRedirect("ViewProductServlet");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}