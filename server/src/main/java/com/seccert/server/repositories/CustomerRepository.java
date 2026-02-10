package com.seccert.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seccert.server.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}