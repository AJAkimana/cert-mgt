package com.seccert.server.controllers;

import com.seccert.server.entity.Certificate;
import com.seccert.server.repositories.CertificateRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateRepository certificateRepository;

    public CertificateController(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @GetMapping
    public List<Certificate> getAll() {
        return certificateRepository.findAll();
    }

    @PostMapping
    public Certificate create(@RequestBody Certificate certificate) {
        return certificateRepository.save(certificate);
    }
}