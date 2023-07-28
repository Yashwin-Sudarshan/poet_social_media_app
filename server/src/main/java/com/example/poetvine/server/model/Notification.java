package com.example.poetvine.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public Notification(User recipient, String message) {
        this.recipient = recipient;
        this.message = message;
        this.isNotificationRead = false;
    }
}
