package com.example.poetvine.server.controller;

import com.example.poetvine.server.exception.ResourceNotFoundException;
import com.example.poetvine.server.exception.UserNotAuthorisedException;
import com.example.poetvine.server.model.Notification;
import com.example.poetvine.server.model.User;
import com.example.poetvine.server.response.NotificationDto;
import com.example.poetvine.server.service.NotificationService;
import com.example.poetvine.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllNotificationsForUser(@AuthenticationPrincipal UserDetails userDetails) {
        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        Set<NotificationDto> notifications;
        try {
            notifications = notificationService.getNotifications(userRequesting).stream()
                    .map(Notification::toNotificationDto)
                    .collect(Collectors.toSet());
        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Map<String, Set<NotificationDto>> response = new HashMap<>();
        response.put("notifications", notifications);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/notification/{id}")
    public ResponseEntity<?> readNotification(
            @PathVariable long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        NotificationDto readNotification;
        try {
            readNotification = notificationService.readNotification(id, userRequesting).toNotificationDto();
        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Map<String, NotificationDto> response = new HashMap<>();
        response.put("notification", readNotification);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/notification/{id}")
    public ResponseEntity<?> deleteNotification(
            @PathVariable long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        NotificationDto deletedNotification;
        try {
            deletedNotification = notificationService.deleteNotification(id, userRequesting).toNotificationDto();
        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Map<String, NotificationDto> response = new HashMap<>();
        response.put("notification", deletedNotification);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        try {
            notificationService.deleteAllNotifications(userRequesting);
        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Notifications successfully deleted.");

        return ResponseEntity.ok(response);
    }
}
