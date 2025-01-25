package com.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.dao.UserDAO;
import com.model.User;

public class ViewAccountServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve user ID from session (or use another mechanism to securely fetch user ID)
        int userId = (int) request.getSession().getAttribute("userId");

        // Fetch user details from the database
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserById(userId);

        if (user != null) {
            // Set the user object in the request attributes
            request.setAttribute("user", user);

            // Forward to ViewAccount.jsp
            RequestDispatcher dispatcher = request.getRequestDispatcher("ViewAccount.jsp");
            dispatcher.forward(request, response);
        } else {
            // Handle the case where user is not found (optional)
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        }
    }
}