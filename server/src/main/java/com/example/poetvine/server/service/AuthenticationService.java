package com.example.poetvine.server.service;

import com.example.poetvine.server.config.security.JwtService;
import com.example.poetvine.server.exception.UserAlreadyExists;
import com.example.poetvine.server.model.User;
import com.example.poetvine.server.model.enumeration.Role;
import com.example.poetvine.server.model.enumeration.VisibilityPreference;
import com.example.poetvine.server.payload.AuthenticationRequest;
import com.example.poetvine.server.payload.RegisterRequest;
import com.example.poetvine.server.repository.UserRepository;
import com.example.poetvine.server.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        // Check if username or email already exists
        Optional<User> userWithExistingUsername = userRepository.findByUsername(request.getUsername());
        Optional<User> userWithExistingEmail = userRepository.findByEmail(request.getEmail());
        if (userWithExistingUsername.isPresent()) {
            throw new UserAlreadyExists("Username already exists");
        }
        if (userWithExistingEmail.isPresent()) {
            throw new UserAlreadyExists("Email already exists");
        }

        User user = new User(
            request.getEmail(),
            request.getUsername(),
            passwordEncoder.encode(request.getPassword()),
            null,
            null,
            null,
            VisibilityPreference.PUBLIC,
            VisibilityPreference.PUBLIC,
            Role.USER
        );

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse
            .builder()
            .token(jwtToken)
            .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse
            .builder()
            .token(jwtToken)
            .build();
    }
}
