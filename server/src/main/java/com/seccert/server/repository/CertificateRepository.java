package com.seccert.server.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seccert.server.entity.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
    List<Certificate> findAllByCustomerId(UUID customerId);
}