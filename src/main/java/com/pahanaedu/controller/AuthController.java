package com.pahanaedu.controller;

import com.pahanaedu.model.User;
import com.pahanaedu.service.AuthService;
import com.pahanaedu.util.JSONUtil;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "AuthController", value = "/api/auth/*")
public class AuthController extends HttpServlet {
    private AuthService authService = new AuthService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/login")) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            User user = authService.authenticate(username, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_OK,
                        "Login successful", user);
            } else {
                JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "Invalid username or password", null);
            }
        } else if (pathInfo.equals("/logout")) {
            request.getSession().invalidate();
            JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_OK,
                    "Logout successful", null);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}