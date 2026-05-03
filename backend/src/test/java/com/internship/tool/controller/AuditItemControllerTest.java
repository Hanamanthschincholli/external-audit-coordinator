package com.internship.tool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.tool.dto.AuditItemDTO;
import com.internship.tool.dto.CreateAuditItemRequest;
import com.internship.tool.service.AuditItemService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuditItemControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private AuditItemService auditItemService;
    private AuditItemController auditItemController;

    @BeforeEach
    void setUp() {

        // ✅ MANUAL MOCK (NO @Mock)
        auditItemService = mock(AuditItemService.class);

        // ✅ MANUAL INJECTION
        auditItemController = new AuditItemController(auditItemService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(auditItemController)
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateAuditItem() throws Exception {

        CreateAuditItemRequest request = new CreateAuditItemRequest();
        request.setTitle("Test Audit");
        request.setStatus("OPEN");

        AuditItemDTO response = new AuditItemDTO();
        response.setId(UUID.randomUUID());
        response.setTitle("Test Audit");
        response.setStatus("OPEN");

        when(auditItemService.createAuditItem(any(), anyString()))
                .thenReturn(response);

        mockMvc.perform(post("/api/audit-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "USER1")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Audit"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }
}