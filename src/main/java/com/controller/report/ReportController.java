package com.controller.report;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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

        System.out.println("Report Type: " + reportType);
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);

        // Store selected report type to be used in JSP
        request.setAttribute("selectedReportType", reportType);

        // Generate the report data
        String reportData = "";

        if ("sales".equals(reportType)) {
            reportData = reportService.generateSalesReport(startDate, endDate);
            request.setAttribute("salesReportData", reportData);
            System.out.println("Sales Report Data: " + reportData);
        } else if ("inventory".equals(reportType)) {
            reportData = reportService.generateInventoryReport();
            request.setAttribute("inventoryReportData", reportData);
            System.out.println("Inventory Report Data: " + reportData);
        }

        // Forward to JSP
        request.getRequestDispatcher("Report.jsp").forward(request, response);
    }
}
