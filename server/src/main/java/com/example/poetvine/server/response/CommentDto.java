package com.example.poetvine.server.response;

import com.example.poetvine.server.annotation.CustomDateTimeFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    @JsonProperty("comment_id")
    private Long commentId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("poem_id")
    private Long poemId;

    @JsonProperty("message")
    private String message;

    @JsonProperty("commented_at")
    @CustomDateTimeFormat
    private LocalDateTime commentedAt;
}
