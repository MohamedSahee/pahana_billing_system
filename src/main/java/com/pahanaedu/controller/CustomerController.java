package com.pahanaedu.controller;

import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.model.Customer;
import com.pahanaedu.service.CustomerService;
import com.pahanaedu.util.JSONUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CustomerController", value = "/api/customers/*")
public class CustomerController extends HttpServlet {
    private CustomerService customerService = new CustomerService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all customers
            List<Customer> customers = customerService.getAllCustomers();
            JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_OK,
                    "Customers retrieved", customers);
        } else {
            // Get customer by ID
            String accountNumber = pathInfo.substring(1);
            Customer customer = customerService.getCustomerByAccountNumber(accountNumber);
            if (customer != null) {
                JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_OK,
                        "Customer found", customer);
            } else {
                JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_NOT_FOUND,
                        "Customer not found", null);
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Customer customer = JSONUtil.parseJsonRequest(request, Customer.class);
        if (customer != null) {
            boolean created = customerService.createCustomer(customer);
            if (created) {
                JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_CREATED,
                        "Customer created", customer);
            } else {
                JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                        "Failed to create customer", null);
            }
        } else {
            JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid customer data", null);
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accountNumber = request.getPathInfo().substring(1);
        Customer customer = JSONUtil.parseJsonRequest(request, Customer.class);

        if (customer != null && accountNumber.equals(customer.getAccountNumber())) {
            boolean updated = customerService.updateCustomer(customer);
            if (updated) {
                JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_OK,
                        "Customer updated", customer);
            } else {
                JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                        "Failed to update customer", null);
            }
        } else {
            JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid customer data", null);
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accountNumber = request.getPathInfo().substring(1);
        boolean deleted = customerService.deleteCustomer(accountNumber);
        if (deleted) {
            JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_OK,
                    "Customer deleted", null);
        } else {
            JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_NOT_FOUND,
                    "Customer not found", null);
        }
    }
}