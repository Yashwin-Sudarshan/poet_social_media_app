package com.example.poetvine.server.model;

import com.example.poetvine.server.annotation.CustomDateTimeFormat;
import com.example.poetvine.server.model.enumeration.PoemStatus;
import com.example.poetvine.server.response.PoemDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "poems")
public class Poem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long poemId;

    @NotNull
    @NotBlank
    private String title;

    @Column(length = Integer.MAX_VALUE)
    private String content;

    private String[] tags;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PoemStatus poemStatus;

    @CustomDateTimeFormat
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "poemLiked",  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();;

    @OneToMany(mappedBy = "poemCommentedOn",  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();;

    public Poem(String title, String content, String[] tags, User author, PoemStatus poemStatus) {
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.author = author;
        this.poemStatus = poemStatus;
    }

    public PoemDto toPoemDto() {
        PoemDto poemDto = new PoemDto();
        poemDto.setId(poemId);
        poemDto.setTitle(title);
        poemDto.setContent(content);
        poemDto.setAuthorUsername(author.getUsername());
        poemDto.setCreatedAt(createdAt);
        poemDto.setTags(tags);
        poemDto.setNumberOfLikes(likes.size());
        poemDto.setNumberOfComments(comments.size());
        return poemDto;
    }

    public void addLike(Like like) {
        likes.add(like);
    }

    public void removeLike(Like like) {
        likes.remove(like);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(poemId);
    }

}
