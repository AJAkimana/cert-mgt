package com.seccert.server.controller;

import com.seccert.server.dto.certificate.GenerateCertificateRequest;
import com.seccert.server.dto.certificate.GenerateCertificateResponse;
import com.seccert.server.dto.common.ApiResponse;
import com.seccert.server.entity.Certificate;
import com.seccert.server.repository.CertificateRepository;
import com.seccert.server.service.CertificateService;
import com.seccert.server.service.ResponseService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateRepository certificateRepository;
    private final CertificateService certificateService;
    private final ResponseService responseService;

    public CertificateController(CertificateRepository certificateRepository,
            CertificateService certificateService,
            ResponseService responseService) {
        this.certificateRepository = certificateRepository;
        this.certificateService = certificateService;
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

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<GenerateCertificateResponse>> generate(
            @Valid @RequestBody GenerateCertificateRequest request,
            Authentication authentication) {
        String identifier = authentication != null ? authentication.getName() : null;
        GenerateCertificateResponse response = certificateService.enqueueGeneration(request, identifier);
        return ResponseEntity.ok(responseService.success("Certificate generation queued", response));
    }
}