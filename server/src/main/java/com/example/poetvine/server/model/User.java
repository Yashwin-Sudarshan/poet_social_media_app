package com.example.poetvine.server.model;

import com.example.poetvine.server.model.enumeration.VisibilityPreference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull
    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    @NotBlank
    @Column(unique = true)
    private String username;

    @NotNull
    @NotBlank
    private String password;

    private String profileImageName;

    private String bio;

    private String[] topicsWrittenAbout;

    @NotNull
    private VisibilityPreference profileVisibilityPreference;

    @NotNull
    private VisibilityPreference poemVisibilityPreference;

    @ManyToMany
    @JoinTable(
            name = "users_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<User> followers = new HashSet<>();

    @ManyToMany(mappedBy = "followers")
    private Set<User> following = new HashSet<>();

    @OneToMany(mappedBy = "author",  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Poem> poemsWritten = new HashSet<>();;

    @ManyToMany
    @JoinTable(
            name = "users_saved_poems",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "poem_id")
    )
    private Set<Poem> poemsSaved = new HashSet<>();

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    private Set<Notification> notifications = new HashSet<>();

    public User(String email, String username, String password, String profileImageName, String bio, String[] topicsWrittenAbout,
                VisibilityPreference profileVisibilityPreference, VisibilityPreference poemVisibilityPreference) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.profileImageName = profileImageName;
        this.bio = bio;
        this.topicsWrittenAbout = topicsWrittenAbout;
        this.profileVisibilityPreference = profileVisibilityPreference;
        this.poemVisibilityPreference = poemVisibilityPreference;
    }

    public void followUser(User user) {
        following.add(user);
        user.getFollowers().add(this); // Bidirectional relationship
    }

    public void unfollowUser(User user) {
        following.remove(user);
        user.getFollowers().remove(this);
    }

    public void addPoemWritten(Poem poem) {
        poemsWritten.add(poem);
    }

    public void removePoemWritten(Poem poem) {
        poemsWritten.remove(poem);
    }

    public void addPoemSaved(Poem poem) {
        poemsSaved.add(poem);
    }

    public void removePoemSaved(Poem poem) {
        poemsSaved.remove(poem);
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
        notification.setRecipient(this);
    }

    public void removeNotification(Notification notification) {
        notifications.remove(notification);
        notification.setRecipient(null);
    }

    public void removeAllNotifications() {
        for (Notification notification : notifications) {
            notification.setRecipient(null);
        }
        notifications.clear();
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId); // Use a unique field for hashCode calculation
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        return Objects.equals(userId, other.userId); // Use a unique field for equality comparison
    }
}