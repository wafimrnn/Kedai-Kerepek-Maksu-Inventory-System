package com.controller.sale;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dao.SaleDAO;
import com.manager.DBConnection;
import com.model.Sale;
import com.model.SaleItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class CompleteSaleServlet extends HttpServlet {
    private SaleDAO saleDAO;

    @Override
    public void init() {
        saleDAO = new SaleDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        System.out.println("CompleteSaleServlet reached!");

        // Read JSON input
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        System.out.println("Received JSON: " + sb.toString()); // Debugging log

        try {
            JSONObject requestData = new JSONObject(sb.toString());

            // Extract sale details
            double totalAmount = requestData.getDouble("totalAmount");
            String paymentMethod = requestData.getString("paymentMethod");
            String saleDate = requestData.getString("saleDate");

            // Get userId from session
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                throw new Exception("User not logged in.");
            }
            int userId = (Integer) session.getAttribute("userId");

            // Create Sale object
            Sale sale = new Sale();
            sale.setSaleDate(saleDate);
            sale.setTotalAmount(totalAmount);
            sale.setPaymentMethod(paymentMethod);
            sale.setUserId(userId);

            // Extract and validate order items
            JSONArray orderItems = requestData.getJSONArray("orderItems");
            if (orderItems.length() == 0) {
                throw new Exception("Sale items cannot be empty.");
            }

            List<SaleItem> saleItems = new ArrayList<>();
            for (int i = 0; i < orderItems.length(); i++) {
                JSONObject item = orderItems.getJSONObject(i);
                SaleItem saleItem = new SaleItem();
                saleItem.setProdId(item.getInt("prodId"));
                saleItem.setQuantity(item.getInt("qty"));
                saleItem.setSubTotal(item.getDouble("subtotal"));
                saleItems.add(saleItem);
            }
            sale.setSaleItems(saleItems);

            // Insert sale and sale items
            int saleId = saleDAO.insertSale(sale);
            try (Connection conn = DBConnection.getConnection()) {
                saleDAO.insertSaleItems(conn, saleId, saleItems);
            }

            // Send success response
            JSONObject responseJson = new JSONObject();
            responseJson.put("status", "success");
            responseJson.put("message", "Sale completed successfully!");
            response.getWriter().write(responseJson.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Log error
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            JSONObject errorResponse = new JSONObject();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            response.getWriter().write(errorResponse.toString());
        }
    }
}
