package com.example.poetvine.server.service;

import com.example.poetvine.server.exception.ResourceNotFoundException;
import com.example.poetvine.server.exception.UserNotAuthorisedException;
import com.example.poetvine.server.model.Notification;
import com.example.poetvine.server.model.User;
import com.example.poetvine.server.repository.NotificationRepository;
import com.example.poetvine.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    private Notification findById(long notificationId) {
        if (notificationRepository.findById(notificationId).isPresent()) return notificationRepository.findById(notificationId).get();
        else throw new ResourceNotFoundException("Notification not found.");
    }

    public Set<Notification> getNotifications(User userRequesting) {
        if (userRequesting == null) throw new ResourceNotFoundException("Notifications not found.");
        return notificationRepository.findAllByRecipient(userRequesting);
    }

    public void createNotification(User recipient, String message) {
        if (recipient == null || message == null) throw new NullPointerException("Recipient or message cannot be null.");

        Notification notification = new Notification(recipient, message);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public Notification readNotification(long id, User userRequesting) {
        Notification notification = this.findById(id);

        if (userRequesting != null && userRequesting.getNotifications().contains(notification)) {
            notification.setNotificationRead(true);
            notificationRepository.save(notification);
        }
        else throw new UserNotAuthorisedException("User not permitted to perform function.");

        return notification;
    }

    public Notification deleteNotification(long id, User userRequesting) {
        Notification notification = this.findById(id);

        if (userRequesting != null && userRequesting.getNotifications().contains(notification)) {
            userRequesting.removeNotification(notification);
            notificationRepository.delete(notification);
        }
        else throw new UserNotAuthorisedException("User not permitted to perform function.");

        return notification;
    }

    public void deleteAllNotifications(User userRequesting) {
        if (userRequesting != null) {
            notificationRepository.deleteAll(getNotifications(userRequesting));
        }
        else throw new UserNotAuthorisedException("User not permitted to perform function.");
    }
}
