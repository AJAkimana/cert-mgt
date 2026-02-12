package com.seccert.server.dto.certificate;

import java.util.UUID;

public class GenerateCertificateResponse {

    private UUID certificateId;
    private String status;

    public GenerateCertificateResponse(UUID certificateId, String status) {
        this.certificateId = certificateId;
        this.status = status;
    }

    public UUID getCertificateId() {
        return certificateId;
    }

    public String getStatus() {
        return status;
    }
}
