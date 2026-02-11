package com.seccert.server.controller;

import com.seccert.server.dto.auth.*;
import com.seccert.server.dto.common.ApiResponse;
import com.seccert.server.service.AuthService;
import com.seccert.server.service.ResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final ResponseService responseService;

    public AuthController(AuthService authService, ResponseService responseService) {
        this.authService = authService;
        this.responseService = responseService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(responseService.success("Login successful", authService.login(request)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(responseService.success("Registration successful"));
    }
}