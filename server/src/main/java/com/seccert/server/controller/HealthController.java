package com.seccert.server.controller;

import com.seccert.server.dto.common.ApiResponse;
import com.seccert.server.service.ResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    private final ResponseService responseService;

    public HealthController(ResponseService responseService) {
        this.responseService = responseService;
    }

    @GetMapping("/api/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(responseService.success("OK", "healthy"));
    }
}
