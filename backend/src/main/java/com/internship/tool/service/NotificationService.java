package com.internship.tool.service;

import com.internship.tool.entity.Notification;
import com.internship.tool.entity.User;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public NotificationService(NotificationRepository notificationRepository,
                               UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }

    public Page<Notification> getAllNotifications(Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }

    public Notification getNotificationById(UUID id) {
        return notificationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Notification not found with id: " + id));
    }

    public List<Notification> getUserNotifications(UUID userId) {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getUnreadNotifications(UUID userId) {
        return notificationRepository.findByRecipientIdAndReadFalseOrderByCreatedAtDesc(userId);
    }

    public long getUnreadCount(UUID userId) {
        return notificationRepository.countByRecipientIdAndReadFalse(userId);
    }

    @Transactional
    public Notification createNotification(UUID recipientId, UUID senderId, Notification notification) {

        User recipient = userService.getUserById(recipientId);
        notification.setRecipient(recipient);

        if (senderId != null) {
            User sender = userService.getUserById(senderId);
            notification.setSender(sender);
        }

        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    @Transactional
    public Notification markAsRead(UUID id) {
        Notification notification = getNotificationById(id);

        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    @Transactional
    public int markAllAsReadForUser(UUID userId) {
        return notificationRepository.markAllAsRead(userId);
    }

    @Transactional
    public void deleteNotification(UUID id) {
        Notification notification = getNotificationById(id);
        notificationRepository.delete(notification);
    }
}
