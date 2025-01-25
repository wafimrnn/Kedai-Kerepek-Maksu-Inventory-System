package com.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import com.dao.SaleDAO;
import com.itextpdf.text.List;

/**
 * Servlet implementation class DashboardServlet
 */
@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SaleDAO saleDAO = new SaleDAO();
        List sales = null;
        try {
            sales = (List) saleDAO.getAllSales();  // This method should return a list of sales
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set the 'sales' list as a request attribute
        request.setAttribute("sales", sales);
        
        // Forward the request to the dashboard.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("DashboardHome.jsp");
        dispatcher.forward(request, response);
    }
}
