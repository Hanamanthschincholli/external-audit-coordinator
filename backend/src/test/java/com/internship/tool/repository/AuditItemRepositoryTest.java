package com.internship.tool.repository;

<<<<<<< HEAD
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
=======
import com.internship.tool.entity.AuditItem;
>>>>>>> 1a0f3e0 (Add controller tests)
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

<<<<<<< HEAD
import com.internship.tool.entity.AuditItem;
import com.internship.tool.entity.User;
=======
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
>>>>>>> 1a0f3e0 (Add controller tests)

@DataJpaTest
@ActiveProfiles("test")
class AuditItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuditItemRepository auditItemRepository;

    @Test
    void shouldFindActiveByStatus() {
<<<<<<< HEAD
        AuditItem item1 = createSimpleAuditItem("Test1", "OPEN", false);
        AuditItem item2 = createSimpleAuditItem("Test2", "CLOSED", false);
        AuditItem item3 = createSimpleAuditItem("Test3", "OPEN", true);
=======
        // Given
        AuditItem item1 = createAuditItem("Test1", "OPEN", false);
        AuditItem item2 = createAuditItem("Test2", "CLOSED", false);
        AuditItem item3 = createAuditItem("Test3", "OPEN", true); // deleted
>>>>>>> 1a0f3e0 (Add controller tests)

        entityManager.persistAndFlush(item1);
        entityManager.persistAndFlush(item2);
        entityManager.persistAndFlush(item3);

<<<<<<< HEAD
        Page<AuditItem> result =
                auditItemRepository.findActiveByStatus("OPEN", PageRequest.of(0, 10));

=======
        // When
        Page<AuditItem> result = auditItemRepository.findActiveByStatus("OPEN", PageRequest.of(0, 10));

        // Then
>>>>>>> 1a0f3e0 (Add controller tests)
        assertThat(result.getContent()).hasSize(1)
                .extracting(AuditItem::getTitle)
                .containsExactly("Test1");
    }

    @Test
    void shouldFindByFilters() {
<<<<<<< HEAD

        User user1 = new User();
        user1.setId(UUID.randomUUID());

        User user2 = new User();
        user2.setId(UUID.randomUUID());

        entityManager.persistAndFlush(user1);
        entityManager.persistAndFlush(user2);

        AuditItem item1 = createUserAuditItem("Audit A", "OPEN", "HIGH", user1, false);
        AuditItem item2 = createUserAuditItem("Audit B", "CLOSED", "LOW", user2, false);
        AuditItem item3 = createUserAuditItem("Audit A", "OPEN", "MEDIUM", user1, true);
=======
        // Given
        AuditItem item1 = createAuditItem("Audit A", "OPEN", "HIGH", "user1", false);
        AuditItem item2 = createAuditItem("Audit B", "CLOSED", "LOW", "user2", false);
        AuditItem item3 = createAuditItem("Audit A", "OPEN", "MEDIUM", "user1", true); // deleted
>>>>>>> 1a0f3e0 (Add controller tests)

        entityManager.persistAndFlush(item1);
        entityManager.persistAndFlush(item2);
        entityManager.persistAndFlush(item3);

<<<<<<< HEAD
        Page<AuditItem> result =
                auditItemRepository.findByFilters(
                        "%Audit A%", "OPEN", "HIGH", user1.getId(), PageRequest.of(0, 10)
                );

        assertThat(result.getContent()).hasSize(1);
=======
        // When
        Page<AuditItem> result = auditItemRepository.findByFilters("%Audit A%", "OPEN", "HIGH", "user1", PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).hasSize(1)
                .first()
                .satisfies(item -> {
                    assertThat(item.getTitle()).isEqualTo("Audit A");
                    assertThat(item.isDeleted()).isFalse();
                });
>>>>>>> 1a0f3e0 (Add controller tests)
    }

    @Test
    void shouldSoftDeleteById() {
<<<<<<< HEAD
        AuditItem item = createSimpleAuditItem("Test", "OPEN", false);
        entityManager.persistAndFlush(item);
        UUID id = item.getId();

        auditItemRepository.softDeleteById(id);
        entityManager.flush();

        AuditItem deleted = entityManager.find(AuditItem.class, id);
        entityManager.refresh(deleted);

=======
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
>>>>>>> 1a0f3e0 (Add controller tests)
        assertThat(deleted).isNotNull();
        assertThat(deleted.isDeleted()).isTrue();
    }

<<<<<<< HEAD
    private AuditItem createSimpleAuditItem(String title, String status, boolean deleted) {
        AuditItem item = new AuditItem();
        item.setId(UUID.randomUUID());
        item.setTitle(title);
        item.setStatus(status);
        item.setDeleted(deleted);
        return item;
    }

    private AuditItem createUserAuditItem(String title, String status, String priority, User assignedTo, boolean deleted) {
        AuditItem item = new AuditItem();
        item.setId(UUID.randomUUID());
=======
    private AuditItem createAuditItem(String title, String status, boolean deleted) {
        return createAuditItem(title, status, null, null, deleted);
    }

    private AuditItem createAuditItem(String title, String status, String priority, String assignedTo, boolean deleted) {
        AuditItem item = new AuditItem();
>>>>>>> 1a0f3e0 (Add controller tests)
        item.setTitle(title);
        item.setStatus(status);
        item.setPriority(priority);
        item.setAssignedTo(assignedTo);
        item.setDeleted(deleted);
        return item;
    }
}
<<<<<<< HEAD

=======
>>>>>>> 1a0f3e0 (Add controller tests)
