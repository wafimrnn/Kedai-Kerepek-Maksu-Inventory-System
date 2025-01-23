package com.controller;

import com.dao.AccountDAO;
import com.model.Account;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateAccountServlet extends HttpServlet {
    private final AccountDAO accountDAO = new AccountDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get accountId from URL
        int accountId = Integer.parseInt(request.getParameter("accountId"));
        Account account = accountDAO.getAccountById(accountId);
        if (account != null) {
            request.setAttribute("account", account);
            request.getRequestDispatcher("UpdateAccount.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Account not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle form submission to update the account
        int accountId = Integer.parseInt(request.getParameter("accountId"));
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String status = request.getParameter("status");

        Account account = new Account();
        account.setAccountId(accountId);
        account.setUsername(username);
        account.setEmail(email);
        account.setStatus(status);

        try {
            accountDAO.updateAccount(account);
            response.sendRedirect("ViewAccountServlet"); // Redirect to view account list
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to update account");
        }
    }
}