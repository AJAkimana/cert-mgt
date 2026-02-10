package com.seccert.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seccert.server.models.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
}