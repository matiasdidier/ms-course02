package com.bcp.pe.customer.service;

import com.bcp.pe.customer.repository.entity.Customer;
import com.bcp.pe.customer.repository.entity.Region;

import java.util.List;

public interface CustomerService {
    public List<Customer> findCustomerAll();
    public List<Customer> findCustomersByRegion(Region region);
    public Customer createCustomer(Customer customer);
    public Customer updateCustomer(Customer customer);
    public Customer findCustomerByEmail(String email);
    public Customer deleteCustomer(Customer customer);
    public  Customer getCustomer(Long id);
}
