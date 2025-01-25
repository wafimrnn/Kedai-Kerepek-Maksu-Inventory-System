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
        // Get the logged-in user's ID from the session
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId"); // Use Integer to handle null safely

        if (userId == null) {
            System.out.println("User ID not found in session. Redirecting to login page.");
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
            request.setAttribute("accStatus", "Active"); // Assuming status is 'Active'
        }

        // Forward the request to viewAccount.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("ViewAccount.jsp");
        dispatcher.forward(request, response);
    }
}
