package com.internship.tool.controller;

<<<<<<< HEAD
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

=======
>>>>>>> 1a0f3e0 (Add controller tests)
import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.tool.dto.AuditItemDTO;
import com.internship.tool.dto.CreateAuditItemRequest;
import com.internship.tool.service.AuditItemService;

<<<<<<< HEAD
=======
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

>>>>>>> 1a0f3e0 (Add controller tests)
class AuditItemControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private AuditItemService auditItemService;
    private AuditItemController auditItemController;

    @BeforeEach
    void setUp() {

<<<<<<< HEAD
        auditItemService = mock(AuditItemService.class);

=======
        // ✅ MANUAL MOCK (NO @Mock)
        auditItemService = mock(AuditItemService.class);

        // ✅ MANUAL INJECTION
>>>>>>> 1a0f3e0 (Add controller tests)
        auditItemController = new AuditItemController(auditItemService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(auditItemController)
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateAuditItem() throws Exception {

<<<<<<< HEAD
        // Request
=======
>>>>>>> 1a0f3e0 (Add controller tests)
        CreateAuditItemRequest request = new CreateAuditItemRequest();
        request.setTitle("Test Audit");
        request.setStatus("OPEN");

<<<<<<< HEAD
        // Response mock
=======
>>>>>>> 1a0f3e0 (Add controller tests)
        AuditItemDTO response = new AuditItemDTO();
        response.setId(UUID.randomUUID());
        response.setTitle("Test Audit");
        response.setStatus("OPEN");

<<<<<<< HEAD
        // ✅ FIXED: Correct matcher usage
        when(auditItemService.createAuditItem(any(CreateAuditItemRequest.class), any(UUID.class)))
=======
        when(auditItemService.createAuditItem(any(), anyString()))
>>>>>>> 1a0f3e0 (Add controller tests)
                .thenReturn(response);

        mockMvc.perform(post("/api/audit-items")
                        .contentType(MediaType.APPLICATION_JSON)
<<<<<<< HEAD
                        .header("X-User-Id", UUID.randomUUID().toString()) // send valid UUID
=======
                        .header("X-User-Id", "USER1")
>>>>>>> 1a0f3e0 (Add controller tests)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Audit"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }
}