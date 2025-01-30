package com.controller.report;

import java.sql.Date;
import java.util.List;

import com.dao.ProductDAO;
import com.dao.ReportDAO;
import com.dao.SaleDAO;
import com.model.InventoryReport;
import com.model.Product;
import com.model.Sale;
import com.model.SalesReport;

import java.util.List;

public class ReportService {
    private SaleDAO saleDAO = new SaleDAO();
    private ProductDAO productDAO = new ProductDAO();

    // Generate sales report based on date range
    public String generateSalesReport(String startDate, String endDate) {
        // Query the database for sales data within the date range
        List<Sale> sales = saleDAO.getSalesReport(startDate, endDate);

        // Convert the sales data into a formatted string (or you could return it as JSON, PDF, etc.)
        StringBuilder report = new StringBuilder();
        report.append("<h2>Sales Report (" + startDate + " to " + endDate + ")</h2>");
        report.append("<table><tr><th>Sale ID</th><th>Sale Date</th><th>Total Amount</th><th>Payment Method</th></tr>");
        for (Sale sale : sales) {
            report.append("<tr><td>").append(sale.getSaleId()).append("</td>")
                    .append("<td>").append(sale.getSaleDate()).append("</td>")
                    .append("<td>").append(sale.getTotalAmount()).append("</td>")
                    .append("<td>").append(sale.getPaymentMethod()).append("</td></tr>");
        }
        report.append("</table>");
        return report.toString();
    }

    // Generate inventory report
    public String generateInventoryReport() {
        // Query the database for product inventory data
        List<Product> products = productDAO.getInventoryReport();

        StringBuilder report = new StringBuilder();
        report.append("<h2>Inventory Report</h2>");
        report.append("<table><tr><th>Product Name</th><th>Price</th><th>Stock Quantity</th></tr>");
        for (Product product : products) {
            report.append("<tr><td>").append(product.getProdName()).append("</td>")
                    .append("<td>").append(product.getProdPrice()).append("</td>")
                    .append("<td>").append(product.getQuantityStock()).append("</td></tr>");
        }
        report.append("</table>");
        return report.toString();
    }
}