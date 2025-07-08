package com.controller.account;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.dao.UserDAO;

public class SignUpServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("userName").trim();
        String password = request.getParameter("password").trim();
        String phone = request.getParameter("phone").trim();
        String address = request.getParameter("address").trim();

        // === Input validation ===
        boolean isUsernameValid = username.matches("^[a-zA-Z0-9]+$");            // Alphanumeric only
        boolean isPasswordValid = password.length() >= 6;                         // At least 6 chars
        boolean isPhoneValid = phone.matches("^[0-9]{10,11}$");                   // 10-11 digits
        boolean isAddressValid = address.matches("^[a-zA-Z0-9\\s,\\.\\-]+$");     // Valid characters

        if (!isUsernameValid || !isPasswordValid || !isPhoneValid || !isAddressValid) {
            response.sendRedirect("Login.html?message=Invalid input. Please enter the correct data type for each field.");
            return;
        }

        UserDAO userDAO = new UserDAO();

        try {
            // Check if username already exists
            if (userDAO.isUsernameTaken(username)) {
                response.sendRedirect("Login.html?message=Username is already taken. Please choose another.");
                return;
            }

            // Attempt to create the account
            boolean accountCreated = userDAO.createOwner(username, password, phone, address);

            if (accountCreated) {
                response.sendRedirect("Login.html?message=Account created successfully! Please log in.");
            } else {
                response.sendRedirect("Login.html?message=Error creating account. Please try again later.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("Login.html?message=An unexpected error occurred. Please try again.");
        }
    }
}
