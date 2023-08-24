package com.example.poetvine.server.controller;

import com.example.poetvine.server.config.security.JwtService;
import com.example.poetvine.server.exception.ResourceNotFoundException;
import com.example.poetvine.server.exception.UserNotAuthorisedException;
import com.example.poetvine.server.model.User;
import com.example.poetvine.server.payload.EditUserRequest;
import com.example.poetvine.server.response.MyUserDto;
import com.example.poetvine.server.response.OtherUserDto;
import com.example.poetvine.server.service.MapValidationErrorService;
import com.example.poetvine.server.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;

    private final MapValidationErrorService mapValidationErrorService;

    @GetMapping("/profile/{username}")
    public ResponseEntity<?> getUserProfile(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        User userProfileToView = null;
        try {
            userProfileToView = userService.getUserProfile(userRequesting, username);

        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);

        }

        if (userProfileToView.equals(userRequesting)) {
            Map<String, MyUserDto> response = new HashMap<>();
            response.put("user", userProfileToView.toMyUserDto());
            return ResponseEntity.ok(response);
        }

        Map<String, OtherUserDto> response = new HashMap<>();
        response.put("user", userProfileToView.toOtherUserDto());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/profile/{username}")
    public ResponseEntity<?> editUserProfile(
            @PathVariable String username,
            @Valid @RequestBody EditUserRequest editUserRequest,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationErrorService(result);
        if (errorMap != null) {
            return errorMap;
        }

        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        User updatedUser;
        try {
            updatedUser = userService.editUserProfile(userRequesting, username, editUserRequest);
        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        String updatedJwtToken = jwtService.generateToken(updatedUser);

        Map<String, Object> response = new HashMap<>();
        response.put("user", updatedUser.toMyUserDto());
        response.put("token", updatedJwtToken);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/profile/{username}")
    public ResponseEntity<?> deleteUserProfile(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        User deletedUser;
        try {
            deletedUser = userService.deleteUserProfile(userRequesting, username);
        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Map<String, MyUserDto> response = new HashMap<>();
        response.put("user", deletedUser.toMyUserDto());

        return ResponseEntity.ok(response);
    }
}
