package com.seccert.server.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.seccert.server.dto.certificate.GenerateCertificateRequest;
import com.seccert.server.dto.certificate.GenerateCertificateResponse;
import com.seccert.server.entity.Certificate;
import com.seccert.server.entity.CertificateFile;
import com.seccert.server.entity.Template;
import com.seccert.server.entity.User;
import com.seccert.server.repository.CertificateFileRepository;
import com.seccert.server.repository.CertificateRepository;
import com.seccert.server.repository.TemplateRepository;
import com.seccert.server.repository.UserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final CertificateFileRepository certificateFileRepository;
    private final TemplateRepository templateRepository;
    private final UserRepository userRepository;

    public CertificateService(CertificateRepository certificateRepository,
            CertificateFileRepository certificateFileRepository,
            TemplateRepository templateRepository,
            UserRepository userRepository) {
        this.certificateRepository = certificateRepository;
        this.certificateFileRepository = certificateFileRepository;
        this.templateRepository = templateRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public GenerateCertificateResponse enqueueGeneration(GenerateCertificateRequest request, String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new AccessDeniedException("Unauthenticated request");
        }

        User user = userRepository.findByEmailOrUsername(identifier, identifier)
                .orElseThrow(() -> new AccessDeniedException("User not found"));

        Template template = templateRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new IllegalArgumentException("Template not found"));

        if (user.getCustomer() == null || !user.getCustomer().getId().equals(template.getCustomer().getId())) {
            throw new AccessDeniedException("Not allowed to generate for this template");
        }

        String recipientName = resolveRecipientName(request);
        if (recipientName == null || recipientName.isBlank()) {
            recipientName = user.getCustomer().getName();
        }

        Certificate certificate = new Certificate();
        certificate.setCustomer(user.getCustomer());
        certificate.setTemplate(template);
        certificate.setRecipientName(recipientName);
        certificate.setRecipientEmail(request.getRecipientEmail());
        certificate.setStatus("PROCESSING");
        certificate.setDataPayload(request.getData());
        certificate = certificateRepository.save(certificate);

        generateCertificateAsync(certificate.getId(), template, request.getData());

        return new GenerateCertificateResponse(certificate.getId(), certificate.getStatus());
    }

    @Async("certificateTaskExecutor")
    public void generateCertificateAsync(UUID certificateId, Template template, Map<String, Object> data) {
        Optional<Certificate> certOptional = certificateRepository.findById(certificateId);
        if (certOptional.isEmpty()) {
            return;
        }
        Certificate certificate = certOptional.get();
        try {
            String html = renderTemplate(template.getRawTemplate(), data);
            Path storageDir = Path.of("storage", "certificates");
            Files.createDirectories(storageDir);
            Path filePath = storageDir.resolve(certificateId + ".pdf");

            try (OutputStream outputStream = new FileOutputStream(filePath.toFile())) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.withHtmlContent(html, null);
                builder.toStream(outputStream);
                builder.run();
            }

            String checksum = sha256Base64(filePath);
            CertificateFile certificateFile = new CertificateFile();
            certificateFile.setCertificate(certificate);
            certificateFile.setFileUrl(filePath.toAbsolutePath().toString());
            certificateFile.setChecksum(checksum);
            certificateFileRepository.save(certificateFile);

            certificate.setStatus("READY");
            certificate.setIssuedAt(Instant.now());
            certificateRepository.save(certificate);
        } catch (Exception ex) {
            certificate.setStatus("FAILED");
            certificateRepository.save(certificate);
        }
    }

    private String resolveRecipientName(GenerateCertificateRequest request) {
        if (request.getRecipientName() != null && !request.getRecipientName().isBlank()) {
            return request.getRecipientName();
        }
        Object fullName = request.getData().get("fullName");
        return fullName == null ? null : fullName.toString();
    }

    private String renderTemplate(String rawTemplate, Map<String, Object> data) {
        if (rawTemplate == null) {
            return "";
        }
        String rendered = rawTemplate;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String token = "{" + entry.getKey() + "}";
            String value = entry.getValue() == null ? "" : entry.getValue().toString();
            rendered = rendered.replace(token, value);
        }
        rendered = rendered.replace("&nbsp;", "&#160;");
        return wrapHtml(rendered);
    }

    private String wrapHtml(String body) {
        return "<!DOCTYPE html>" +
                "<html><head><meta charset=\"UTF-8\"/></head><body>" +
                body +
                "</body></html>";
    }

    private String sha256Base64(Path filePath) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = Files.readAllBytes(filePath);
        byte[] hash = digest.digest(bytes);
        return Base64.getEncoder().encodeToString(hash);
    }
}
