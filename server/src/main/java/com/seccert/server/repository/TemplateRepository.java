package com.seccert.server.repository;

import com.seccert.server.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TemplateRepository extends JpaRepository<Template, UUID> {
    List<Template> findAllByCustomerId(UUID customerId);
}
