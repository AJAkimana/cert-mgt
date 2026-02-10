package com.seccert.server.models;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "certificates")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String serialNumber;

    @Column(nullable = false)
    private String type; // e.g. SSL, Signing, Client

    private Instant issuedAt;

    private Instant expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Certificate() {
    }

    // getters and setters
}