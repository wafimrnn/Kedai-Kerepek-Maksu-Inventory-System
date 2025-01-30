package com.controller.report;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/ReportController")
public class ReportController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReportService reportService = new ReportService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get parameters from the form
        String reportType = request.getParameter("reportType");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        // Generate the report based on the type
        String report = "";

        if ("sales".equals(reportType)) {
            // Generate sales report
            report = reportService.generateSalesReport(startDate, endDate);
            request.setAttribute("salesReportData", report);
        } else if ("inventory".equals(reportType)) {
            // Generate inventory report
            report = reportService.generateInventoryReport();
            request.setAttribute("inventoryReportData", report);
        }

        // Forward to JSP
        request.getRequestDispatcher("Report.jsp").forward(request, response);
    }
}