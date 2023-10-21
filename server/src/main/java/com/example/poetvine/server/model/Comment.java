package com.example.poetvine.server.model;

import com.example.poetvine.server.annotation.CustomDateTimeFormat;
import com.example.poetvine.server.response.CommentDto;
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

    public CommentDto toCommentDto() {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentId(commentId);
        commentDto.setUsername(commentedByUser.getUsername());
        commentDto.setPoemId(poemCommentedOn.getPoemId());
        commentDto.setMessage(message);
        commentDto.setCommentedAt(commentedAt);
        return commentDto;
    }
}
