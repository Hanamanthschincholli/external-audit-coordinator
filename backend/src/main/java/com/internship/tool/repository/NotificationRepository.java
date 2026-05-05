package com.internship.tool.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.internship.tool.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByRecipientIdOrderByCreatedAtDesc(UUID recipientId);

    List<Notification> findByRecipientIdAndReadFalseOrderByCreatedAtDesc(UUID recipientId);

    long countByRecipientIdAndReadFalse(UUID recipientId);

    @Query("SELECT n FROM Notification n WHERE n.notificationType = :notificationType AND n.recipient.id = :recipientId")
    List<Notification> findByNotificationTypeAndRecipientId(
            @Param("notificationType") String notificationType, 
            @Param("recipientId") UUID recipientId);

    @Modifying
    @Transactional
    @Query("""
            UPDATE Notification n
               SET n.read = true, n.readAt = CURRENT_TIMESTAMP
             WHERE n.recipient.id = :recipientId
               AND n.read = false
            """)
    int markAllAsRead(@Param("recipientId") UUID recipientId);
}
