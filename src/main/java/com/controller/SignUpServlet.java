package com.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.dao.UserDAO;

public class SignUpServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");
        String address = request.getParameter("address");

        try {
            // Check if any users exist in the database
            boolean isFirstAccount = userDAO.countUsers() == 0;

            if (isFirstAccount) {
                // Allow creating the first owner account without session validation
                if (!"OWNER".equals(role)) {
                    response.getWriter().write("Only the first account can be an OWNER!");
                    return;
                }
                if (userDAO.createUser(username, password, phone, role, address, null)) {
                    response.getWriter().write("First owner account created successfully!");
                } else {
                    response.getWriter().write("Failed to create the first owner account!");
                }
            } else {
                // Normal account creation process
                HttpSession session = request.getSession(false);
                if (session == null || !"OWNER".equals(session.getAttribute("userRole"))) {
                    response.getWriter().write("Unauthorized access!");
                    return;
                }

                int ownerId = (int) session.getAttribute("userId");

                if (userDAO.isUsernameTaken(username)) {
                    response.getWriter().write("Username already exists!");
                    return;
                }

                if (userDAO.createUser(username, password, phone, role, address, ownerId)) {
                    response.getWriter().write("Account created successfully!");
                } else {
                    response.getWriter().write("Failed to create account!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("An error occurred: " + e.getMessage());
        }
    }
}