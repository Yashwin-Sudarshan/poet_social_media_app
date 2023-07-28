package com.example.poetvine.server.data;

import com.example.poetvine.server.model.*;
import com.example.poetvine.server.model.enumeration.PoemStatus;
import com.example.poetvine.server.model.enumeration.VisibilityPreference;
import com.example.poetvine.server.repository.*;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.util.*;

public class MockDataGenerator {

    private static final int NUMBER_OF_USERS = 20;
    private static final int NUMBER_OF_POEMS = 50;
    private static final int NUMBER_OF_LIKES = 100;
    private static final int NUMBER_OF_COMMENTS = 20;
    private static final int NUMBER_OF_NOTIFICATIONS = 15;

    private final Faker faker;
    private final UserRepository userRepository;
    private final PoemRepository poemRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;

    public MockDataGenerator(UserRepository userRepository, PoemRepository poemRepository,
                             LikeRepository likeRepository, CommentRepository commentRepository,
                             NotificationRepository notificationRepository) {
        this.faker = new Faker();
        this.userRepository = userRepository;
        this.poemRepository = poemRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.notificationRepository = notificationRepository;
    }

    public void generateMockData() {
        List<User> users = generateUsers();
        List<Poem> poems = generatePoems(users);
        generateLikes(poems, users);
        generateComments(poems, users);
        generateNotifications(users);
    }

    private List<User> generateUsers() {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_USERS; i++) {
            User user = new User(
                    faker.internet().emailAddress(),
                    faker.name().username(),
                    faker.internet().password(),
                    faker.avatar().image(),
                    faker.lorem().sentence(),
                    faker.lorem().words(3).toArray(new String[0]),
                    VisibilityPreference.PUBLIC,
                    VisibilityPreference.PUBLIC
            );

            users.add(user);
        }

        generateFollowRelationships(users, 30);

        userRepository.saveAll(users);

        return users;
    }

    private void generateFollowRelationships(List<User> users, int numFollows) {
        Set<String> followRelationships = new HashSet<>();
        Random random = new Random();

        while (followRelationships.size() < numFollows) {
            int followerIndex = random.nextInt(users.size());
            int userIndex = random.nextInt(users.size());

            if (followerIndex != userIndex) {
                String followRelationship = followerIndex + "-" + userIndex;
                if (!followRelationships.contains(followRelationship)) {
                    followRelationships.add(followRelationship);
                    users.get(followerIndex).followUser(users.get(userIndex));
                }
            }
        }
    }

    private List<Poem> generatePoems(List<User> users) {
        List<Poem> poems = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_POEMS; i++) {
            User author = getRandomUser(users);

            Poem poem = new Poem(
                    faker.lorem().sentence(),
                    faker.lorem().paragraph(),
                    faker.lorem().words(3).toArray(new String[0]),
                    author,
                    PoemStatus.PUBLISHED
            );

            poem.setCreatedAt(generateRandomDateTime());
            poemRepository.save(poem);
            poems.add(poem);

            author.addPoemWritten(poem);
            userRepository.save(author);
        }

        return poems;
    }

    private void generateLikes(List<Poem> poems, List<User> users) {
        for (int i = 0; i < NUMBER_OF_LIKES; i++) {
            User user = getRandomUser(users);
            Poem poem = getRandomPoem(poems);

            Like like = new Like(user, poem);
            likeRepository.save(like);

            poem.addLike(like);
            poemRepository.save(poem);
        }
    }

    private void generateComments(List<Poem> poems, List<User> users) {
        for (int i = 0; i < NUMBER_OF_COMMENTS; i++) {
            User user = getRandomUser(users);
            Poem poem = getRandomPoem(poems);

            Comment comment = new Comment(user, poem, faker.lorem().sentence());
            comment.setCommentedAt(generateRandomDateTime());
            commentRepository.save(comment);

            poem.addComment(comment);
            poemRepository.save(poem);
        }
    }

    private void generateNotifications(List<User> users) {
        for (int i = 0; i < NUMBER_OF_NOTIFICATIONS; i++) {
            User recipient = getRandomUser(users);
            Notification notification = new Notification(recipient, faker.lorem().sentence());
            notificationRepository.save(notification);

            recipient.addNotification(notification);
            userRepository.save(recipient);
        }
    }

    private User getRandomUser(List<User> users) {
        int index = faker.random().nextInt(users.size());
        return users.get(index);
    }

    private Poem getRandomPoem(List<Poem> poems) {
        int index = faker.random().nextInt(poems.size());
        return poems.get(index);
    }

    private LocalDateTime generateRandomDateTime() {
        Random random = new Random();
        long daysAgo = random.nextInt(60) + 1; // Generates a random number between 1 and 60 (inclusive)
        long hoursAgo = random.nextInt(24);    // Generates a random number between 0 and 23 (inclusive)

        return LocalDateTime.now().minusDays(daysAgo).minusHours(hoursAgo);
    }
}

