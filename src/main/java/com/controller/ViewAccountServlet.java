package com.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.dao.UserDAO;
import com.model.User;

public class ViewAccountServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the logged-in user's ID from the session
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            // Redirect to login page if no user is logged in
            response.sendRedirect("Login.html?message=Please log in to view your account.");
            return;
        }

        // Fetch user details using DAO
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserById(userId);

        if (user != null) {
            // Set user details in request attributes
            request.setAttribute("userName", user.getName());
            request.setAttribute("userPhone", user.getPhone());
            request.setAttribute("userAddress", user.getAddress());
            request.setAttribute("userRole", user.getRole());
            request.setAttribute("accStatus", "Active"); // Assuming 'Active' status for now

            // Also forward user role to request scope for JSP
            request.setAttribute("userRole", user.getRole());
        } else {
            // If user retrieval fails, redirect to error page
            response.sendRedirect("Error.jsp?message=Unable to retrieve account details.");
            return;
        }

        // Forward the request to viewAccount.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("viewAccount.jsp");
        dispatcher.forward(request, response);
    }
}
