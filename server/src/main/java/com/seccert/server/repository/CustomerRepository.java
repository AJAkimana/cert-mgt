package com.seccert.server.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seccert.server.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}