package com.example.poetvine.server.service;

import com.example.poetvine.server.exception.ResourceNotFoundException;
import com.example.poetvine.server.exception.UserNotAuthorisedException;
import com.example.poetvine.server.model.User;
import com.example.poetvine.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByUsername(String username) {
        if (userRepository.findByUsername(username).isPresent()) return userRepository.findByUsername(username).get();
        else throw new ResourceNotFoundException("User not found");
    }
}
