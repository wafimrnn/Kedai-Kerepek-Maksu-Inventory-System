package com.controller.sale;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import com.dao.SaleDAO;
import com.model.Sale;
import com.model.SaleItem;

@WebServlet("/generateReceipt")
public class ReceiptServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        int saleId = Integer.parseInt(request.getParameter("saleId"));
        SaleDAO saleDAO = new SaleDAO();
        Sale sale = saleDAO.getSaleWithItems(saleId);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Receipt</title></head><body>");
        out.println("<h2>Receipt</h2>");
        out.println("<p>Sale ID: " + sale.getSaleId() + "</p>");
        out.println("<p>Date: " + sale.getSaleDate() + "</p>");
        out.println("<p>Payment Method: " + sale.getPaymentMethod() + "</p>");
        out.println("<hr>");
        out.println("<h3>Items Purchased</h3>");
        out.println("<table border='1'><tr><th>Product</th><th>Quantity</th><th>Subtotal</th></tr>");
        
        for (SaleItem item : sale.getSaleItems()) {
            out.println("<tr><td>" + item.getProdName() + "</td><td>" + item.getQuantity() + 
                        "</td><td>$" + item.getSubTotal() + "</td></tr>");
        }

        out.println("</table>");
        out.println("<hr>");
        out.println("<p><b>Total: $" + sale.getTotalAmount() + "</b></p>");
        out.println("<button onclick='window.print()'>Print Receipt</button>");
        out.println("</body></html>");
    }
}