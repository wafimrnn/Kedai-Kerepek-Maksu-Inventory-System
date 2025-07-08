package com.controller.account;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.dao.UserDAO;
import com.model.User;

public class CreateStaffServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser != null && "OWNER".equals(currentUser.getRole())) {
            String username = request.getParameter("userName");
            String password = request.getParameter("password");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            UserDAO userDAO = new UserDAO();
            boolean isCreated = userDAO.createStaff(username, password, phone, address, currentUser.getId());

            if (isCreated) {
                // ✅ Redirect to servlet with success message
                response.sendRedirect("ViewAccountServlet?success=Staff account created successfully!");
            } else {
                // ❌ Redirect to servlet with error message
                response.sendRedirect("ViewAccountServlet?error=Failed to create staff account.");
            }
        } else {
            // 🚫 Not logged in or not an OWNER — redirect to login
            response.sendRedirect("Login.html");
        }
    }
}



