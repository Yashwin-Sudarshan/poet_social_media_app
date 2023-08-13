package com.example.poetvine.server.data;

import com.example.poetvine.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PoemRepository poemRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public DataLoader(UserRepository userRepository, PoemRepository poemRepository,
                      LikeRepository likeRepository, CommentRepository commentRepository,
                      NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.poemRepository = poemRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        MockDataGenerator mockDataGenerator = new MockDataGenerator(userRepository, poemRepository,
                likeRepository, commentRepository, notificationRepository);
        mockDataGenerator.generateMockData();
    }
}

