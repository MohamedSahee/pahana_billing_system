package com.pahanaedu.service;

import com.pahanaedu.dao.BillDAO;
import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.dao.ItemDAO;
import com.pahanaedu.model.Bill;
import com.pahanaedu.model.BillItem;
import com.pahanaedu.model.Customer;
import com.pahanaedu.model.Item;

import java.math.BigDecimal;
import java.util.List;

public class BillService {
    private BillDAO billDAO;
    private CustomerDAO customerDAO;
    private ItemDAO itemDAO;

    public BillService() {
        this.billDAO = new BillDAO();
        this.customerDAO = new CustomerDAO();
        this.itemDAO = new ItemDAO();
    }

    public Bill createBill(Bill bill) {
        // Validate customer
        Customer customer = customerDAO.findByAccountNumber(bill.getCustomerId());
        if (customer == null) {
            return null;
        }

        // Validate items and calculate total
        BigDecimal total = BigDecimal.ZERO;
        for (BillItem billItem : bill.getItems()) {
            Item item = itemDAO.findById(billItem.getItemId());
            if (item == null) {
                return null;
            }
            billItem.setUnitPrice(item.getUnitPrice());
            BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(billItem.getQuantity()));
            billItem.setTotal(itemTotal);
            total = total.add(itemTotal);
        }

        // Apply discount if any
        if (bill.getDiscountPercent() != null && bill.getDiscountPercent() > 0) {
            BigDecimal discount = total.multiply(BigDecimal.valueOf(bill.getDiscountPercent() / 100.0));
            bill.setDiscountAmount(discount);
            total = total.subtract(discount);
        }

        bill.setTotal(total);

        // Save bill to database
        if (billDAO.create(bill)) {
            return bill;
        }

        return null;
    }

    public Bill getBillById(int id) {
        return billDAO.findById(id);
    }

    public List<Bill> getBillsByCustomer(String customerId) {
        return billDAO.findByCustomerId(customerId);
    }
}