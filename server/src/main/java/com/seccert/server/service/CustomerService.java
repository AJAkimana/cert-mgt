package com.seccert.server.service;

import com.seccert.server.dto.customer.CreateCustomerRequest;
import com.seccert.server.entity.Customer;
import com.seccert.server.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer createCustomer(CreateCustomerRequest request) {
        if (customerRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Customer name already exists");
        }

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        return customerRepository.save(customer);
    }
}
