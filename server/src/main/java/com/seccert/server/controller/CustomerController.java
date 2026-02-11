package com.seccert.server.controller;

import com.seccert.server.dto.common.ApiResponse;
import com.seccert.server.dto.customer.CreateCustomerRequest;
import com.seccert.server.entity.Customer;
import com.seccert.server.service.CustomerService;
import com.seccert.server.service.ResponseService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final ResponseService responseService;

    public CustomerController(CustomerService customerService, ResponseService responseService) {
        this.customerService = customerService;
        this.responseService = responseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Customer>>> getAll() {
        return ResponseEntity.ok(responseService.success("Customers loaded", customerService.getAllCustomers()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Customer>> create(@Valid @RequestBody CreateCustomerRequest request) {
        Customer saved = customerService.createCustomer(request);
        return ResponseEntity.ok(responseService.success("Customer created", saved));
    }
}