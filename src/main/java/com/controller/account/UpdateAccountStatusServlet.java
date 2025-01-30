package com.controller.account;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.dao.UserDAO;

public class UpdateAccountStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO = new UserDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("userRole");

        // Only OWNER can update staff accounts
        if (!"OWNER".equals(userRole)) {
            response.getWriter().write("unauthorized");
            return;
        }

        int staffId = Integer.parseInt(request.getParameter("staffId"));
        String newStatus = request.getParameter("newStatus");

        boolean updated = userDAO.updateAccountStatus(staffId, newStatus);

        if (updated) {
            response.getWriter().write("success");
        } else {
            response.getWriter().write("error");
        }
    }
}
