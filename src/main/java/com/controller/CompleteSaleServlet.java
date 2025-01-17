package com.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dao.SaleDAO;
import com.model.Sale;
import com.model.SaleItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CompleteSaleServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Parse JSON request body
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject requestData = new JSONObject(sb.toString());

            // Extract sale details
            double totalAmount = requestData.getDouble("totalAmount");
            String paymentMethod = requestData.getString("paymentMethod");
            int userId = requestData.getInt("userId");
            String saleDate = requestData.getString("saleDate");

            // Validate and convert saleDate
            Date saleDateObj = Date.valueOf(saleDate); // Throws IllegalArgumentException if invalid

            // Create Sale object
            Sale sale = new Sale();
            sale.setSaleDate(saleDateObj); // Convert to SQL Date
            sale.setTotalAmount(totalAmount);
            sale.setPaymentMethod(paymentMethod);
            sale.setUserId(userId);

            // Extract sale items
            JSONArray orderItems = requestData.getJSONArray("orderItems");
            List<SaleItem> saleItems = new ArrayList<>();
            for (int i = 0; i < orderItems.length(); i++) {
                JSONObject item = orderItems.getJSONObject(i);

                SaleItem saleItem = new SaleItem();
                saleItem.setProductId(item.getInt("prodId"));  // Use prodId instead of productId
                saleItem.setQuantity(item.getInt("qty"));
                saleItem.setSubtotal(item.getDouble("subtotal"));
                saleItems.add(saleItem);
            }

            // Persist sale and sale items using DAO
            SaleDAO saleDAO = new SaleDAO();
            int saleId = saleDAO.insertSale(sale); // Insert sale and get ID
            for (SaleItem item : saleItems) {
                item.setSaleId(saleId); // Set sale ID for each item
            }
            saleDAO.insertSaleItems(saleItems);

            // Respond with success
            JSONObject responseJson = new JSONObject();
            responseJson.put("status", "success");
            responseJson.put("message", "Sale completed successfully!");
            response.getWriter().write(responseJson.toString());

        } catch (IllegalArgumentException e) {
            // Handle invalid date format
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Invalid date format. Please provide the date in yyyy-MM-dd format.");
            response.getWriter().write(errorResponse.toString());
        } catch (Exception e) {
            // Handle other errors and send structured error response
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error: " + e.getMessage());
            response.getWriter().write(errorResponse.toString());
        }
    }
}
