package com.example.poetvine.server.model;

import com.example.poetvine.server.annotation.CustomDateTimeFormat;
import com.example.poetvine.server.response.NotificationDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @NotNull
    @NotBlank
    private String message;

    @NotNull
    private boolean isNotificationRead;

    @CustomDateTimeFormat
    private LocalDateTime createdAt;

    public Notification(User recipient, String message) {
        this.recipient = recipient;
        this.message = message;
        this.isNotificationRead = false;
    }

    public NotificationDto toNotificationDto() {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setNotificationId(notificationId);
        notificationDto.setRecipient(recipient.getUsername());
        notificationDto.setMessage(message);
        notificationDto.setRead(isNotificationRead);
        notificationDto.setCreatedAt(createdAt);
        return notificationDto;
    }
}
