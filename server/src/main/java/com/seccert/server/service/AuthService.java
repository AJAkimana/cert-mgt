package com.seccert.server.service;

import com.seccert.server.dto.auth.*;
import com.seccert.server.entity.Customer;
import com.seccert.server.entity.User;
import com.seccert.server.repository.CustomerRepository;
import com.seccert.server.repository.UserRepository;
import com.seccert.server.security.JwtUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthService(CustomerRepository customerRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authManager,
            JwtUtil jwtUtil) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse login(LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getIdentifier(), request.getPassword()));
        String token = jwtUtil.generateToken(request.getIdentifier());
        return new AuthResponse(token);
    }

    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already used");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already used");
        }

        Customer customer = new Customer();
        customer.setName(request.getCustomerName());
        customer.setEmail(request.getCustomerEmail());
        customerRepository.save(customer);

        User user = new User();
        user.setCustomer(customer);
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole("ADMIN");
        userRepository.save(user);
    }
}