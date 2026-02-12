package com.seccert.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seccert.server.config.SecurityConfig;
import com.seccert.server.dto.template.CreateTemplateRequest;
import com.seccert.server.entity.Template;
import com.seccert.server.security.JwtAuthFilter;
import com.seccert.server.service.TemplateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TemplateController.class)
@Import(SecurityConfig.class)
class TemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TemplateService templateService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void createTemplate_requiresAuthentication() throws Exception {
        CreateTemplateRequest request = new CreateTemplateRequest();
        request.setName("Template");
        request.setRawTemplate("Hello {fullName}");

        mockMvc.perform(post("/api/templates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "tester")
    void createTemplate_returnsTemplate() throws Exception {
        CreateTemplateRequest request = new CreateTemplateRequest();
        request.setName("Template");
        request.setRawTemplate("Hello {fullName}");
        request.setPlaceholders(Map.of("fullName", Map.of("type", "text")));

        Template template = new Template();
        template.setName("Template");
        template.setRawTemplate("Hello {fullName}");
        template.setPlaceholders(request.getPlaceholders());
        template.setActive(true);

        Mockito.when(templateService.createTemplate(any(CreateTemplateRequest.class), eq("tester")))
                .thenReturn(template);

        mockMvc.perform(post("/api/templates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Template"));
    }

    @Test
    @WithMockUser(username = "tester")
    void listTemplates_returnsTemplates() throws Exception {
        Template template = new Template();
        template.setName("Template A");
        template.setRawTemplate("Hello {fullName}");

        Mockito.when(templateService.getTemplatesForUser("tester"))
                .thenReturn(List.of(template));

        mockMvc.perform(get("/api/templates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("Template A"));
    }
}
