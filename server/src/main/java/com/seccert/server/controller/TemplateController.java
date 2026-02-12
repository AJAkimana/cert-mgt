package com.seccert.server.controller;

import com.seccert.server.dto.common.ApiResponse;
import com.seccert.server.dto.template.CreateTemplateRequest;
import com.seccert.server.entity.Template;
import com.seccert.server.service.ResponseService;
import com.seccert.server.service.TemplateService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    private final TemplateService templateService;
    private final ResponseService responseService;

    public TemplateController(TemplateService templateService, ResponseService responseService) {
        this.templateService = templateService;
        this.responseService = responseService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Template>> create(
            @Valid @RequestBody CreateTemplateRequest request,
            Authentication authentication) {
        System.out.println("======>Received request to create template: " + request.getName());
        String identifier = authentication != null ? authentication.getName() : null;
        Template template = templateService.createTemplate(request, identifier);
        return ResponseEntity.ok(responseService.success("Template created", template));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Template>>> list(Authentication authentication) {
        System.out.println("Received request to list templates");
        String identifier = authentication != null ? authentication.getName() : null;
        List<Template> templates = templateService.getTemplatesForUser(identifier);
        return ResponseEntity.ok(responseService.success("Templates loaded", templates));
    }
}
