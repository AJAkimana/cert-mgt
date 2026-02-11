package com.seccert.server.controller;

import com.seccert.server.dto.common.ApiResponse;
import com.seccert.server.entity.Customer;
import com.seccert.server.repository.CustomerRepository;
import com.seccert.server.service.ResponseService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final ResponseService responseService;

    public CustomerController(CustomerRepository customerRepository, ResponseService responseService) {
        this.customerRepository = customerRepository;
        this.responseService = responseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Customer>>> getAll() {
        return ResponseEntity.ok(responseService.success("Customers loaded", customerRepository.findAll()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Customer>> create(@RequestBody Customer customer) {
        Customer saved = customerRepository.save(customer);
        return ResponseEntity.ok(responseService.success("Customer created", saved));
    }
}