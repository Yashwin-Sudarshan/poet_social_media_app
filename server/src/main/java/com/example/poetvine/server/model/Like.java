package com.example.poetvine.server.model;

import com.example.poetvine.server.annotation.CustomDateTimeFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User likedByUser;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "poem_id")
    private Poem poemLiked;

    @CustomDateTimeFormat
    private LocalDateTime likedAt;

    public Like(User likedByUser, Poem poemLiked) {
        this.likedByUser = likedByUser;
        this.poemLiked = poemLiked;
    }

    @Override
    public int hashCode() {
        return Objects.hash(likeId);
    }

}
