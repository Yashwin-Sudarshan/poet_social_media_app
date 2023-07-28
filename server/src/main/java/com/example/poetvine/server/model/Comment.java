package com.example.poetvine.server.model;

import com.example.poetvine.server.annotation.CustomDateTimeFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User commentedByUser;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "poem_id")
    private Poem poemCommentedOn;

    @NotNull
    @NotBlank
    @Column(length = Integer.MAX_VALUE)
    private String message;

    @CustomDateTimeFormat
    private LocalDateTime commentedAt;

    public Comment(User commentedByUser, Poem poemCommentedOn, String message) {
        this.commentedByUser = commentedByUser;
        this.poemCommentedOn = poemCommentedOn;
        this.message = message;
    }
}
