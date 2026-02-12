package com.seccert.server.repository;

import com.seccert.server.entity.CertificateFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CertificateFileRepository extends JpaRepository<CertificateFile, UUID> {
}
