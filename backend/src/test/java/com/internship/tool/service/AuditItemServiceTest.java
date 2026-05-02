package com.internship.tool.service;

import com.internship.tool.dto.CreateAuditItemRequest;
import com.internship.tool.entity.AuditItem;
import com.internship.tool.repository.AuditItemRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AuditItemServiceTest {

    @Mock
    private AuditItemRepository auditItemRepository;

    @InjectMocks
    private AuditItemService auditItemService;

    public AuditItemServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateAuditItem() {

        CreateAuditItemRequest request = new CreateAuditItemRequest();
        request.setTitle("Test");
        request.setStatus("OPEN");

        AuditItem savedItem = new AuditItem();
        savedItem.setId(UUID.randomUUID());
        savedItem.setTitle("Test");
        savedItem.setStatus("OPEN");

        when(auditItemRepository.save(any(AuditItem.class))).thenReturn(savedItem);

        var result = auditItemService.createAuditItem(request, "USER1");

        assertThat(result.getTitle()).isEqualTo("Test");
        verify(auditItemRepository, times(1)).save(any(AuditItem.class));
    }

    @Test
    void shouldGetAuditItemById() {

        UUID id = UUID.randomUUID();

        AuditItem item = new AuditItem();
        item.setId(id);
        item.setTitle("Test");
        item.setDeleted(false);

        when(auditItemRepository.findById(id)).thenReturn(Optional.of(item));

        var result = auditItemService.getAuditItemById(id);

        assertThat(result.getId()).isEqualTo(id);
    }
}