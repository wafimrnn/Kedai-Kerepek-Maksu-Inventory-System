package com.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import com.dao.UserDAO;

public class UpdateAccountServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve user input from the form
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        int userId = (int) request.getSession().getAttribute("userId"); // Assuming user ID is stored in session

        // Create DAO object to handle database update
        UserDAO userDAO = new UserDAO();
        boolean isUpdated = userDAO.updateUserAccount(userId, phone, address);

        // Send JSON response with feedback
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (isUpdated) {
            out.println("{\"message\": \"Account updated successfully!\"}");
        } else {
            out.println("{\"error\": \"An error occurred while updating your account.\"}");
        }
    }
}