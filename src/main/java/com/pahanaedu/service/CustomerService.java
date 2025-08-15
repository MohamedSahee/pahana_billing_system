package com.pahanaedu.service;

import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.model.Customer;
import java.util.List;

public class CustomerService {
    private CustomerDAO customerDAO;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.findAll();
    }

    public Customer getCustomerByAccountNumber(String accountNumber) {
        return customerDAO.findByAccountNumber(accountNumber);
    }

    public boolean createCustomer(Customer customer) {
        return customerDAO.create(customer);
    }

    public boolean updateCustomer(Customer customer) {
        return customerDAO.update(customer);
    }

    public boolean deleteCustomer(String accountNumber) {
        return customerDAO.delete(accountNumber);
    }
}