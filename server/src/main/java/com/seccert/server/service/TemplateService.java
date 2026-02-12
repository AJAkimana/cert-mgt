package com.seccert.server.service;

import com.seccert.server.dto.template.CreateTemplateRequest;
import com.seccert.server.entity.Template;
import com.seccert.server.entity.User;
import com.seccert.server.repository.TemplateRepository;
import com.seccert.server.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final UserRepository userRepository;

    public TemplateService(TemplateRepository templateRepository, UserRepository userRepository) {
        this.templateRepository = templateRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Template createTemplate(CreateTemplateRequest request, String identifier) {
        System.out.println("Creating template with name: " + request.getName() + " for identifier: " + identifier);
        if (identifier == null || identifier.isBlank()) {
            throw new AccessDeniedException("Unauthenticated request");
        }
        User user = userRepository.findByEmailOrUsername(identifier, identifier)
                .orElseThrow(() -> new AccessDeniedException("User not found"));

        if (user.getCustomer() == null) {
            throw new AccessDeniedException("User has no customer");
        }

        Template template = new Template();
        template.setCustomer(user.getCustomer());
        template.setName(request.getName());
        template.setDescription(request.getDescription());
        template.setRawTemplate(request.getRawTemplate());
        template.setPlaceholders(request.getPlaceholders());
        template.setActive(request.getIsActive() == null || request.getIsActive());
        System.out.println("Saving template: " + template.getName() + " for customer: " + user.getCustomer().getName());
        return templateRepository.save(template);
    }

    @Transactional(readOnly = true)
    public List<Template> getTemplatesForUser(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new AccessDeniedException("Unauthenticated request");
        }
        User user = userRepository.findByEmailOrUsername(identifier, identifier)
                .orElseThrow(() -> new AccessDeniedException("User not found"));

        if (user.getCustomer() == null) {
            throw new AccessDeniedException("User has no customer");
        }

        return templateRepository.findAllByCustomerId(user.getCustomer().getId());
    }
}
