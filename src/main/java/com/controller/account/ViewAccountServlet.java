package com.controller.account;

import com.model.User;
import com.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ViewAccountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ViewAccountServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            response.sendRedirect("Login.html"); // Redirect to login page if user is not logged in
            return;
        }

        // Fetch the updated user information from the database
        UserDAO userDAO = new UserDAO();
        User updatedUser = userDAO.getUserById(sessionUser.getId());

        // Update session data with the latest user details
        if (updatedUser != null) {
            session.setAttribute("user", updatedUser);
            session.setAttribute("userRole", updatedUser.getRole()); // ✅ Fix: update userRole in session
        }

        // Fetch staff list (if applicable) and set as request attribute
        List<User> staffList = userDAO.getStaffByOwnerId(sessionUser.getId());
        request.setAttribute("staffList", staffList);

        // Transfer success or error message from query parameter to request attribute
        String success = request.getParameter("success");
        String error = request.getParameter("error");

        if (success != null) {
            request.setAttribute("success", success);
        }
        if (error != null) {
            request.setAttribute("error", error);
        }

        // Forward to the JSP page
        request.getRequestDispatcher("/ViewAccount.jsp").forward(request, response);
    }
}
