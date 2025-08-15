package com.pahanaedu.controller;

import com.pahanaedu.model.Bill;
import com.pahanaedu.service.BillService;
import com.pahanaedu.util.JSONUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "BillController", value = "/api/bills/*")
public class BillController extends HttpServlet {
    private BillService billService = new BillService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            String customerId = request.getParameter("customerId");
            if (customerId != null && !customerId.isEmpty()) {
                List<Bill> bills = billService.getBillsByCustomer(customerId);
                JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_OK,
                        "Bills retrieved", bills);
            } else {
                JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                        "Customer ID required", null);
            }
        } else {
            try {
                int billId = Integer.parseInt(pathInfo.substring(1));
                Bill bill = billService.getBillById(billId);
                if (bill != null) {
                    JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_OK,
                            "Bill found", bill);
                } else {
                    JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_NOT_FOUND,
                            "Bill not found", null);
                }
            } catch (NumberFormatException e) {
                JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid bill ID", null);
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Bill bill = JSONUtil.parseJsonRequest(request, Bill.class);
        if (bill != null) {
            Bill createdBill = billService.createBill(bill);
            if (createdBill != null) {
                JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_CREATED,
                        "Bill created", createdBill);
            } else {
                JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                        "Failed to create bill", null);
            }
        } else {
            JSONUtil.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid bill data", null);
        }
    }
}