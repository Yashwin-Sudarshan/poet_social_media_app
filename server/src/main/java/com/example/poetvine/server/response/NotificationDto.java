package com.example.poetvine.server.response;

import com.example.poetvine.server.annotation.CustomDateTimeFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDto {

    @JsonProperty("notification_id")
    private Long notificationId;

    @JsonProperty("recipient")
    private String recipient;

    @JsonProperty("message")
    private String message;

    @JsonProperty("is_read")
    private boolean isRead;

    @JsonProperty("created_at")
    @CustomDateTimeFormat
    private LocalDateTime createdAt;
}
