package com.seccert.server.service;

import com.seccert.server.dto.template.CreateTemplateRequest;
import com.seccert.server.entity.Customer;
import com.seccert.server.entity.Template;
import com.seccert.server.entity.User;
import com.seccert.server.repository.TemplateRepository;
import com.seccert.server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TemplateServiceTest {

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TemplateService templateService;

    @Captor
    private ArgumentCaptor<Template> templateCaptor;

    private User user;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName("Acme Corp");

        user = new User();
        user.setUsername("test");
        user.setEmail("test@example.com");
        user.setCustomer(customer);
    }

    @Test
    void createTemplate_setsFields_andDefaultsActive() {
        CreateTemplateRequest request = new CreateTemplateRequest();
        request.setName("Employee Certificate");
        request.setDescription("Default template");
        request.setRawTemplate("Hello {fullName}");
        request.setPlaceholders(Map.of("fullName", Map.of("type", "text")));
        request.setIsActive(null);

        when(userRepository.findByEmailOrUsername("test", "test"))
                .thenReturn(Optional.of(user));
        when(templateRepository.save(any(Template.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Template saved = templateService.createTemplate(request, "test");

        verify(templateRepository).save(templateCaptor.capture());
        Template captured = templateCaptor.getValue();

        assertThat(saved).isSameAs(captured);
        assertThat(captured.getCustomer()).isEqualTo(customer);
        assertThat(captured.getName()).isEqualTo("Employee Certificate");
        assertThat(captured.getDescription()).isEqualTo("Default template");
        assertThat(captured.getRawTemplate()).isEqualTo("Hello {fullName}");
        assertThat(captured.getPlaceholders()).containsKey("fullName");
        assertThat(captured.isActive()).isTrue();
    }

    @Test
    void createTemplate_throwsWhenUnauthenticated() {
        CreateTemplateRequest request = new CreateTemplateRequest();
        request.setName("Template");
        request.setRawTemplate("Hello");

        assertThatThrownBy(() -> templateService.createTemplate(request, null))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("Unauthenticated");
    }

    @Test
    void getTemplatesForUser_returnsList() {
        UUID customerId = UUID.randomUUID();
        setCustomerId(customer, customerId);

        when(userRepository.findByEmailOrUsername("test", "test"))
                .thenReturn(Optional.of(user));
        when(templateRepository.findAllByCustomerId(customerId))
                .thenReturn(List.of(new Template()));

        List<Template> result = templateService.getTemplatesForUser("test");

        assertThat(result).hasSize(1);
        verify(templateRepository).findAllByCustomerId(customerId);
    }

    @Test
    void getTemplatesForUser_throwsWhenUserHasNoCustomer() {
        user.setCustomer(null);
        when(userRepository.findByEmailOrUsername("test", "test"))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> templateService.getTemplatesForUser("test"))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("no customer");
    }

    private void setCustomerId(Customer target, UUID id) {
        try {
            Field field = Customer.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(target, id);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            throw new IllegalStateException("Unable to set customer id", ex);
        }
    }
}
