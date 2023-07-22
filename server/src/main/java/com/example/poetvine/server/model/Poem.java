package com.example.poetvine.server.model;

import com.example.poetvine.server.model.enumeration.PoemStatus;
import com.example.poetvine.server.model.enumeration.VisibilityPreference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "poems")
public class Poem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poem_id", nullable = false)
    private Long poemId;
    @Column(nullable = false)
    private String title;

    /** NB: A poem can be drafted without content, but NOT published without content */
    private String content;
    private String tags;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;
    @ManyToMany(mappedBy = "poemsSaved")
    private Set<User> savedByUsers = new HashSet<>();
    @OneToMany
    @JoinTable(
            name = "poems_likes",
            joinColumns = @JoinColumn(name = "poem_id"),
            inverseJoinColumns = @JoinColumn(name = "like_id")
    )
    private Set<Like> likes = new HashSet<>();
    @OneToMany
    @JoinTable(
            name = "poems_comments",
            joinColumns = @JoinColumn(name = "poem_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private Set<Comment> comments = new HashSet<>();
    @Column(name = "status", nullable = false)
    private PoemStatus poemStatus;
    @Column(name = "poem_visibility_preference", nullable = false)
    private VisibilityPreference poemVisibilityPreference;
}
