package com.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

import com.dao.UserDAO;
import com.model.User;

public class UserController extends HttpServlet {
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    // DoGet for showing the user's account details
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId"); // Assuming userId is stored in the session after login

        if (userId != null) {
            try {
                User user = userDAO.getUserById(userId); // Fetch user details from database
                if (user != null) {
                    request.setAttribute("user", user); // Set the user object to the request
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
                    dispatcher.forward(request, response); // Forward to account JSP to display user details
                } else {
                    response.sendRedirect("Login.html"); // Redirect to login if user not found
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("ViewAccount.jsp"); // Redirect to error page if database query fails
            }
        } else {
            response.sendRedirect("Login.html"); // Redirect to login if user is not logged in
        }
    }
}

