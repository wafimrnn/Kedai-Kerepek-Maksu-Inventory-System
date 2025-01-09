package com.controller;

import com.dao.SaleDAO;
import com.model.Sale;
import com.model.SaleItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ProcessSaleServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String paymentMethod = request.getParameter("paymentMethod");
            double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
            int userId = Integer.parseInt(request.getParameter("userId"));

            String[] productIds = request.getParameterValues("productId");
            String[] quantities = request.getParameterValues("quantity");
            String[] subtotals = request.getParameterValues("subtotal");

            List<SaleItem> saleItems = new ArrayList<>();
            for (int i = 0; i < productIds.length; i++) {
                SaleItem item = new SaleItem();
                item.setProductId(Integer.parseInt(productIds[i]));
                item.setQuantity(Integer.parseInt(quantities[i]));
                item.setSubtotal(Double.parseDouble(subtotals[i]));
                saleItems.add(item);
            }

            Sale sale = new Sale();
            sale.setSaleDate(new Date());
            sale.setTotalAmount(totalAmount);
            sale.setPaymentMethod(paymentMethod);
            sale.setUserId(userId);
            sale.setItems(saleItems);

            SaleDAO saleDAO = new SaleDAO();
            int saleId = saleDAO.createSale(sale);
            saleDAO.addSaleItems(saleId, saleItems);
            saleDAO.updateProductStock(saleItems);

            response.sendRedirect("Success.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("Error.jsp");
        }
    }
}
