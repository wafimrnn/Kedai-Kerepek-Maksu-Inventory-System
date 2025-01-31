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
        List<Sale> sales = saleDAO.getSalesReport(startDate, endDate);
        StringBuilder report = new StringBuilder();

        if (sales.isEmpty()) {
            report.append("<tr><td colspan='4'>No sales data available for this period.</td></tr>");
        } else {
            for (Sale sale : sales) {
                report.append("<tr><td>").append(sale.getSaleId()).append("</td>")
                        .append("<td>").append(sale.getSaleDate()).append("</td>")
                        .append("<td>").append(sale.getTotalAmount()).append("</td>")
                        .append("<td>").append(sale.getPaymentMethod()).append("</td></tr>");
            }
        }
        return report.toString();
    }

    // Generate inventory report
    public String generateInventoryReport() {
        List<Product> products = productDAO.getInventoryReport();
        StringBuilder report = new StringBuilder();

        if (products.isEmpty()) {
            report.append("<tr><td colspan='3'>No inventory data available.</td></tr>");
        } else {
            for (Product product : products) {
                report.append("<tr><td>").append(product.getProdName()).append("</td>")
                        .append("<td>").append(product.getProdPrice()).append("</td>")
                        .append("<td>").append(product.getQuantityStock()).append("</td></tr>");
            }
        }
        return report.toString();
    }
}