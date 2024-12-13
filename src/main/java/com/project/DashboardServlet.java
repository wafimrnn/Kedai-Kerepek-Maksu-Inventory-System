package com.project;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Servlet to forward to the dashboard page
@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws jakarta.servlet.ServletException, IOException {
        // Forward the request to the DashboardHome.jsp page
        request.getRequestDispatcher("/DashboardHome.jsp").forward(request, response);
    }
}
