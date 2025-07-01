package com.controller.report;

import com.model.Sale; // Make sure your Sale model is imported
import com.dao.SaleDAO;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@WebServlet("/ReportController")
public class ReportController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReportService reportService = new ReportService();
    private SaleDAO saleDAO = new SaleDAO(); // For PDF data

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reportType = request.getParameter("reportType");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        // Server-side date validation
     // Server-side date validation
        try {
            LocalDate start = LocalDate.parse(startDateStr);
            LocalDate end = LocalDate.parse(endDateStr);
            LocalDate today = LocalDate.now(ZoneId.of("Asia/Kuala_Lumpur")); // Your timezone

            if (end.isAfter(today)) {
                response.setContentType("text/html");
                response.getWriter().println("<script>alert('End date cannot be after today.'); window.history.back();</script>");
                return;
            }

            if (start.isAfter(end)) {
                response.setContentType("text/html");
                response.getWriter().println("<script>alert('Start date cannot be after end date.'); window.history.back();</script>");
                return;
            }

        } catch (Exception e) {
            response.setContentType("text/html");
            response.getWriter().println("<script>alert('Invalid date format.'); window.history.back();</script>");
            return;
        }

        // Generate PDF only
        if ("sales".equals(reportType)) {
            List<Sale> sales = saleDAO.getSalesReport(startDateStr, endDateStr);
            generateSalesPDF(response, sales, startDateStr, endDateStr);
        } else if ("inventory".equals(reportType)) {
            // Optionally implement inventory PDF later
            response.setContentType("text/html");
            response.getWriter().println("<script>alert('PDF download for inventory is not available yet.'); window.history.back();</script>");
        }
    }

    private void generateSalesPDF(HttpServletResponse response, List<Sale> sales, String startDate, String endDate) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=SalesReport_" + startDate + "_to_" + endDate + ".pdf");

        try {
            Document document = new Document();
            OutputStream out = response.getOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Sales Report\n\n", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Date range
            Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            document.add(new Paragraph("Period: " + startDate + " to " + endDate + "\n\n", dateFont));

            // Table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{2, 3, 2, 3});

            table.addCell("Sale ID");
            table.addCell("Sale Date");
            table.addCell("Total Amount");
            table.addCell("Payment Method");

            if (sales.isEmpty()) {
                PdfPCell noDataCell = new PdfPCell(new Phrase("No sales data for this period."));
                noDataCell.setColspan(4);
                noDataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(noDataCell);
            } else {
                for (Sale sale : sales) {
                    table.addCell(String.valueOf(sale.getSaleId()));
                    table.addCell(String.valueOf(sale.getSaleDate()));
                    table.addCell(String.format("RM %.2f", sale.getTotalAmount()));
                    table.addCell(sale.getPaymentMethod());
                }
            }

            document.add(table);
            document.close();
            out.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
