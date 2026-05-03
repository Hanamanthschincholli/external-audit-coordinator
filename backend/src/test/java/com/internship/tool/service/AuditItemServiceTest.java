package com.internship.tool.service;

<<<<<<< HEAD
<<<<<<< HEAD
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.internship.tool.dto.CreateAuditItemRequest;
import com.internship.tool.entity.AuditItem;
import com.internship.tool.repository.AuditItemRepository;

class AuditItemServiceTest {
=======
=======
import com.internship.tool.dto.AuditItemDTO;
>>>>>>> 1a0f3e0 (Add controller tests)
import com.internship.tool.dto.CreateAuditItemRequest;
import com.internship.tool.entity.AuditItem;
import com.internship.tool.repository.AuditItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

<<<<<<< HEAD
public class AuditItemServiceTest {
>>>>>>> c07d053 (Add CI pipline)
=======
class AuditItemServiceTest {
>>>>>>> 1a0f3e0 (Add controller tests)

    @Mock
    private AuditItemRepository auditItemRepository;

    @InjectMocks
    private AuditItemService auditItemService;

<<<<<<< HEAD
<<<<<<< HEAD
    @BeforeEach
    void setUp() {
=======
    public AuditItemServiceTest() {
>>>>>>> c07d053 (Add CI pipline)
=======
    @BeforeEach
    void setUp() {
>>>>>>> 1a0f3e0 (Add controller tests)
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateAuditItem() {
<<<<<<< HEAD
<<<<<<< HEAD
=======

>>>>>>> c07d053 (Add CI pipline)
=======
>>>>>>> 1a0f3e0 (Add controller tests)
        CreateAuditItemRequest request = new CreateAuditItemRequest();
        request.setTitle("Test");
        request.setStatus("OPEN");

        AuditItem savedItem = new AuditItem();
        savedItem.setId(UUID.randomUUID());
        savedItem.setTitle("Test");
        savedItem.setStatus("OPEN");

        when(auditItemRepository.save(any(AuditItem.class))).thenReturn(savedItem);

<<<<<<< HEAD
        // ✅ FIXED: pass UUID, not String
        var result = auditItemService.createAuditItem(request, UUID.randomUUID());
=======
        var result = auditItemService.createAuditItem(request, "USER1");
>>>>>>> c07d053 (Add CI pipline)

        assertThat(result.getTitle()).isEqualTo("Test");
        verify(auditItemRepository, times(1)).save(any(AuditItem.class));
    }

    @Test
    void shouldGetAuditItemById() {
<<<<<<< HEAD
<<<<<<< HEAD
=======

>>>>>>> c07d053 (Add CI pipline)
=======
>>>>>>> 1a0f3e0 (Add controller tests)
        UUID id = UUID.randomUUID();

        AuditItem item = new AuditItem();
        item.setId(id);
        item.setTitle("Test");
        item.setDeleted(false);

        when(auditItemRepository.findById(id)).thenReturn(Optional.of(item));

        var result = auditItemService.getAuditItemById(id);

        assertThat(result.getId()).isEqualTo(id);
    }
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 1a0f3e0 (Add controller tests)

    @Test
    void shouldThrowExceptionWhenItemNotFound() {
        UUID id = UUID.randomUUID();
        when(auditItemRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> auditItemService.getAuditItemById(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Audit item not found");
    }

    @Test
    void shouldUpdateAuditItem() {
        UUID id = UUID.randomUUID();
        CreateAuditItemRequest request = new CreateAuditItemRequest();
        request.setTitle("Updated");

        AuditItem existing = new AuditItem();
        existing.setId(id);
        existing.setTitle("Old");

        AuditItem saved = new AuditItem();
        saved.setId(id);
        saved.setTitle("Updated");

        when(auditItemRepository.findById(id)).thenReturn(Optional.of(existing));
        when(auditItemRepository.save(any())).thenReturn(saved);

<<<<<<< HEAD
        // ✅ FIXED: pass UUID
        var result = auditItemService.updateAuditItem(id, request, UUID.randomUUID());
=======
        var result = auditItemService.updateAuditItem(id, request, "USER1");
>>>>>>> 1a0f3e0 (Add controller tests)

        assertThat(result.getTitle()).isEqualTo("Updated");
        verify(auditItemRepository, times(1)).save(any());
    }

    @Test
    void shouldSoftDeleteAuditItem() {
        UUID id = UUID.randomUUID();
        doNothing().when(auditItemRepository).softDeleteById(id);

        auditItemService.deleteAuditItem(id);

        verify(auditItemRepository, times(1)).softDeleteById(id);
    }

    @Test
    void shouldGetAllAuditItems() {
        Pageable pageable = PageRequest.of(0, 10);
        AuditItem item = new AuditItem();
        item.setId(UUID.randomUUID());
        item.setTitle("Test");
        item.setDeleted(false);

        Page<AuditItem> page = new PageImpl<>(List.of(item), pageable, 1);

        when(auditItemRepository.findAll(pageable)).thenReturn(page);

        var result = auditItemService.getAllAuditItems(pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void shouldGetByStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        AuditItem item = new AuditItem();
        item.setStatus("OPEN");
        item.setDeleted(false);

        Page<AuditItem> page = new PageImpl<>(List.of(item), pageable, 1);

        when(auditItemRepository.findActiveByStatus(eq("OPEN"), eq(pageable))).thenReturn(page);

        var result = auditItemService.getByStatus("OPEN", pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
<<<<<<< HEAD
    void shouldSearchAuditItems() {
        Pageable pageable = PageRequest.of(0, 10);

        AuditItem item = new AuditItem();
        item.setTitle("Search Test");
        item.setStatus("OPEN");
        item.setDeleted(false);

        Page<AuditItem> page = new PageImpl<>(List.of(item), pageable, 1);

        when(auditItemRepository.findByFilters(
                any(), any(), any(), any(), any(Pageable.class)
        )).thenReturn(page);

        var result = auditItemService.searchAuditItems(
                pageable, "Search Test", "OPEN", null, null
        );

        assertThat(result.getContent()).hasSize(1);
    }
=======
>>>>>>> c07d053 (Add CI pipline)
}
=======
void shouldSearchAuditItems() {
    Pageable pageable = PageRequest.of(0, 10);

    AuditItem item = new AuditItem();
    item.setTitle("Search Test");
    item.setStatus("OPEN");
    item.setDeleted(false);

    Page<AuditItem> page = new PageImpl<>(List.of(item), pageable, 1);

    // ✅ Use flexible matchers instead of exact eq()
    when(auditItemRepository.findByFilters(
            any(), any(), any(), any(), any(Pageable.class)
    )).thenReturn(page);

    var result = auditItemService.searchAuditItems(
            pageable, "Search Test", "OPEN", null, null
    );

    assertThat(result.getContent()).hasSize(1);
}
}
>>>>>>> 1a0f3e0 (Add controller tests)
