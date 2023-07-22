package com.example.poetvine.server.model;

import com.example.poetvine.server.model.enumeration.VisibilityPreference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String username;

    /** REFER TO SPRING SECURITY TUTORIAL TO SEE IF THIS IS NEEDED */
    @Column(nullable = false)
    private String password;
    @Column(name="profile_image_name")
    private String profileImageName;
    private String bio;
    @Column(name = "topics_written_about")
    private String topicsWrittenAbout;
    @Column(name = "profile_visibility_preference", nullable = false)
    private VisibilityPreference profileVisibilityPreference;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_user_id")
    )
    private Set<User> followers = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_following",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "following_user_id")
    )
    private Set<User> usersFollowing = new HashSet<>();
    @OneToMany()
    @JoinTable(
            name = "users_poems_written",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "poem_id")
    )
    private Set<Poem> poemsWritten = new HashSet<>();
    @ManyToMany()
    @JoinTable(
            name = "users_saved_poems",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "poem_id")
    )
    private Set<Poem> poemsSaved = new HashSet<>();
    @OneToMany
    @JoinTable(
            name = "users_notifications",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "notification_id")
    )
    private Set<Notification> notifications = new HashSet<>();
}
