package com.seccert.server.controller;

import com.seccert.server.dto.common.ApiResponse;
import com.seccert.server.entity.Certificate;
import com.seccert.server.repository.CertificateRepository;
import com.seccert.server.service.ResponseService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateRepository certificateRepository;
    private final ResponseService responseService;

    public CertificateController(CertificateRepository certificateRepository, ResponseService responseService) {
        this.certificateRepository = certificateRepository;
        this.responseService = responseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Certificate>>> getAll() {
        return ResponseEntity.ok(responseService.success("Certificates loaded", certificateRepository.findAll()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Certificate>> create(@RequestBody Certificate certificate) {
        Certificate saved = certificateRepository.save(certificate);
        return ResponseEntity.ok(responseService.success("Certificate created", saved));
    }
}