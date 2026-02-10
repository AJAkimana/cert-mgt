package com.seccert.server.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "certificate_files")
public class CertificateFile {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "certificate_id", unique = true)
    private Certificate certificate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String fileUrl;

    @Column(length = 128)
    private String checksum;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    // getters/setters
}