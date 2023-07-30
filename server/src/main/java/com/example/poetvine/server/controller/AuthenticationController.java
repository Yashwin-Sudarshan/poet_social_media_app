package com.example.poetvine.server.controller;

import com.example.poetvine.server.exception.UserAlreadyExists;
import com.example.poetvine.server.payload.AuthenticationRequest;
import com.example.poetvine.server.payload.RegisterRequest;
import com.example.poetvine.server.response.AuthenticationResponse;
import com.example.poetvine.server.service.AuthenticationService;
import com.example.poetvine.server.service.MapValidationErrorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final MapValidationErrorService mapValidationErrorService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request,
            BindingResult result
    ) {
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationErrorService(result);
        if (errorMap != null) {
            return errorMap;
        }

        AuthenticationResponse response = null;
        try {
            response = authenticationService.register(request);

        } catch (UserAlreadyExists e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> register(
            @Valid @RequestBody AuthenticationRequest request,
            BindingResult result
    ) {
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationErrorService(result);
        if (errorMap != null) {
            return errorMap;
        }

        AuthenticationResponse response = null;
        try {
            response = authenticationService.authenticate(request);

        } catch (AuthenticationException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(response);
    }
}
