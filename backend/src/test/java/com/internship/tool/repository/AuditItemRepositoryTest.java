package com.internship.tool.repository;

import com.internship.tool.entity.AuditItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AuditItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuditItemRepository auditItemRepository;

    @Test
    void shouldFindActiveByStatus() {
        // Given
        AuditItem item1 = createAuditItem("Test1", "OPEN", false);
        AuditItem item2 = createAuditItem("Test2", "CLOSED", false);
        AuditItem item3 = createAuditItem("Test3", "OPEN", true); // deleted

        entityManager.persistAndFlush(item1);
        entityManager.persistAndFlush(item2);
        entityManager.persistAndFlush(item3);

        // When
        Page<AuditItem> result = auditItemRepository.findActiveByStatus("OPEN", PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).hasSize(1)
                .extracting(AuditItem::getTitle)
                .containsExactly("Test1");
    }

    @Test
    void shouldFindByFilters() {
        // Given
        AuditItem item1 = createAuditItem("Audit A", "OPEN", "HIGH", "user1", false);
        AuditItem item2 = createAuditItem("Audit B", "CLOSED", "LOW", "user2", false);
        AuditItem item3 = createAuditItem("Audit A", "OPEN", "MEDIUM", "user1", true); // deleted

        entityManager.persistAndFlush(item1);
        entityManager.persistAndFlush(item2);
        entityManager.persistAndFlush(item3);

        // When
        Page<AuditItem> result = auditItemRepository.findByFilters("%Audit A%", "OPEN", "HIGH", "user1", PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).hasSize(1)
                .first()
                .satisfies(item -> {
                    assertThat(item.getTitle()).isEqualTo("Audit A");
                    assertThat(item.isDeleted()).isFalse();
                });
    }

    @Test
    void shouldSoftDeleteById() {
        // Given
        AuditItem item = createAuditItem("Test", "OPEN", false);
        entityManager.persistAndFlush(item);
        UUID id = item.getId();

        // When
        auditItemRepository.softDeleteById(id);
        entityManager.flush();

        // Then
        AuditItem deleted = entityManager.find(AuditItem.class, id);
        entityManager.refresh(deleted);
        assertThat(deleted).isNotNull();
        assertThat(deleted.isDeleted()).isTrue();
    }

    private AuditItem createAuditItem(String title, String status, boolean deleted) {
        return createAuditItem(title, status, null, null, deleted);
    }

    private AuditItem createAuditItem(String title, String status, String priority, String assignedTo, boolean deleted) {
        AuditItem item = new AuditItem();
        item.setTitle(title);
        item.setStatus(status);
        item.setPriority(priority);
        item.setAssignedTo(assignedTo);
        item.setDeleted(deleted);
        return item;
    }
}
