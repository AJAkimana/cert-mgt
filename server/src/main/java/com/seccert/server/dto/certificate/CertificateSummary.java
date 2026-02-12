package com.seccert.server.dto.certificate;

import java.time.Instant;
import java.util.UUID;

public class CertificateSummary {

    private UUID id;
    private UUID templateId;
    private String templateName;
    private String recipientName;
    private String recipientEmail;
    private String status;
    private Instant issuedAt;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean hasFile;

    public CertificateSummary(UUID id,
            UUID templateId,
            String templateName,
            String recipientName,
            String recipientEmail,
            String status,
            Instant issuedAt,
            Instant createdAt,
            Instant updatedAt,
            boolean hasFile) {
        this.id = id;
        this.templateId = templateId;
        this.templateName = templateName;
        this.recipientName = recipientName;
        this.recipientEmail = recipientEmail;
        this.status = status;
        this.issuedAt = issuedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.hasFile = hasFile;
    }

    public UUID getId() {
        return id;
    }

    public UUID getTemplateId() {
        return templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public String getStatus() {
        return status;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public boolean isHasFile() {
        return hasFile;
    }
}
