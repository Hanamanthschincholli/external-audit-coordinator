package com.internship.tool.controller;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.tool.dto.AuditItemDTO;
import com.internship.tool.dto.CreateAuditItemRequest;
import com.internship.tool.service.AuditItemService;

class AuditItemControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private AuditItemService auditItemService;
    private AuditItemController auditItemController;

    @BeforeEach
    void setUp() {

        auditItemService = mock(AuditItemService.class);

        auditItemController = new AuditItemController(auditItemService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(auditItemController)
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateAuditItem() throws Exception {

        // Request
        CreateAuditItemRequest request = new CreateAuditItemRequest();
        request.setTitle("Test Audit");
        request.setStatus("OPEN");

        // Response mock
        AuditItemDTO response = new AuditItemDTO();
        response.setId(UUID.randomUUID());
        response.setTitle("Test Audit");
        response.setStatus("OPEN");

        // ✅ FIXED: Correct matcher usage
        when(auditItemService.createAuditItem(any(CreateAuditItemRequest.class), any(UUID.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/audit-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", UUID.randomUUID().toString()) // send valid UUID
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Audit"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }
}