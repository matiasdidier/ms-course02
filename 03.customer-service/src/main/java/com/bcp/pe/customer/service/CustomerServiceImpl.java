package com.bcp.pe.customer.service;

import com.bcp.pe.customer.repository.CustomerRepository;
import com.bcp.pe.customer.entity.Customer;
import com.bcp.pe.customer.entity.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{


    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> findCustomerAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> findCustomersByRegion(Region region) {
        return customerRepository.findByRegion(region);
    }

    @Override
    public Customer createCustomer(Customer customer) {
        Customer customerBD =  customerRepository.findByNumberID(customer.getNumberID());
        if (customerBD != null){
            return customerBD;
        }
        customer.setState("CREATED");
        customerBD = customerRepository.save(customer);
        return customerBD;
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        Customer customerBD = getCustomer(customer.getId());
        if (customerBD == null){
            return null;
        }
        customerBD.setFirstName(customer.getFirstName());
        customerBD.setLastName(customer.getLastName());
        customerBD.setEmail(customer.getEmail());
        customerBD.setPhotoUrl(customer.getPhotoUrl());
        return customerRepository.save(customerBD);
    }

    @Override
    public Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public Customer deleteCustomer(Customer customer) {
        Customer customerBD = getCustomer(customer.getId());
        if (customerBD == null){
            return null;
        }
        customerBD.setState("DELETED");
        return customerRepository.save(customerBD);
    }

    @Override
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElse(null);
    }
}
