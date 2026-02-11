package com.seccert.server.controller;

import com.seccert.server.dto.common.ApiResponse;
import com.seccert.server.entity.User;
import com.seccert.server.repository.UserRepository;
import com.seccert.server.service.ResponseService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final ResponseService responseService;

    public UserController(UserRepository userRepository, ResponseService responseService) {
        this.userRepository = userRepository;
        this.responseService = responseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAll() {
        return ResponseEntity.ok(responseService.success("Users loaded", userRepository.findAll()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> create(@RequestBody User user) {
        User saved = userRepository.save(user);
        return ResponseEntity.ok(responseService.success("User created", saved));
    }
}