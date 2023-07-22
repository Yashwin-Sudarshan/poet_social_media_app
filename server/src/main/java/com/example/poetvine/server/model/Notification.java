package com.example.poetvine.server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", nullable = false)
    private Long notificationId;
    @ManyToOne
    @JoinColumn(name = "recipient_user_id", nullable = false)
    private User recipient;
    @Column(nullable = false)
    private String message;
    @Column(name = "is_notification_read", nullable = false)
    private boolean isNotificationRead;
}
