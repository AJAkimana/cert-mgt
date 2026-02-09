package com.seccert.server.repository;

import com.seccert.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}