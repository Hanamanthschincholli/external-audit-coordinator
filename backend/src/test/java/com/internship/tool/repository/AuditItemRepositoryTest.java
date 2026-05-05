package com.internship.tool.repository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.internship.tool.entity.AuditItem;
import com.internship.tool.entity.User;

@DataJpaTest
@ActiveProfiles("test")
class AuditItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuditItemRepository auditItemRepository;

    @Test
    void shouldFindActiveByStatus() {
        AuditItem item1 = createSimpleAuditItem("Test1", "OPEN", false);
        AuditItem item2 = createSimpleAuditItem("Test2", "CLOSED", false);
        AuditItem item3 = createSimpleAuditItem("Test3", "OPEN", true);

        entityManager.persistAndFlush(item1);
        entityManager.persistAndFlush(item2);
        entityManager.persistAndFlush(item3);

        Page<AuditItem> result =
                auditItemRepository.findActiveByStatus("OPEN", PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1)
                .extracting(AuditItem::getTitle)
                .containsExactly("Test1");
    }

    @Test
    void shouldFindByFilters() {

        User user1 = new User();
        user1.setId(UUID.randomUUID());

        User user2 = new User();
        user2.setId(UUID.randomUUID());

        entityManager.persistAndFlush(user1);
        entityManager.persistAndFlush(user2);

        AuditItem item1 = createUserAuditItem("Audit A", "OPEN", "HIGH", user1, false);
        AuditItem item2 = createUserAuditItem("Audit B", "CLOSED", "LOW", user2, false);
        AuditItem item3 = createUserAuditItem("Audit A", "OPEN", "MEDIUM", user1, true);

        entityManager.persistAndFlush(item1);
        entityManager.persistAndFlush(item2);
        entityManager.persistAndFlush(item3);

        Page<AuditItem> result =
                auditItemRepository.findByFilters(
                        "%Audit A%", "OPEN", "HIGH", user1.getId(), PageRequest.of(0, 10)
                );

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void shouldSoftDeleteById() {
        AuditItem item = createSimpleAuditItem("Test", "OPEN", false);
        entityManager.persistAndFlush(item);
        UUID id = item.getId();

        auditItemRepository.softDeleteById(id);
        entityManager.flush();

        AuditItem deleted = entityManager.find(AuditItem.class, id);
        entityManager.refresh(deleted);

        assertThat(deleted).isNotNull();
        assertThat(deleted.isDeleted()).isTrue();
    }

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
        item.setTitle(title);
        item.setStatus(status);
        item.setPriority(priority);
        item.setAssignedTo(assignedTo);
        item.setDeleted(deleted);
        return item;
    }
}

