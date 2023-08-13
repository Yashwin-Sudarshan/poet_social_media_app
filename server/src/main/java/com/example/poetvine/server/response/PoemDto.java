package com.example.poetvine.server.response;

import com.example.poetvine.server.annotation.CustomDateTimeFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PoemDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;

    @JsonProperty("author_username")
    private String authorUsername;

    @JsonProperty("created_at")
    @CustomDateTimeFormat
    private LocalDateTime createdAt;

    @JsonProperty("tags")
    private String[] tags;

    @JsonProperty("number_of_likes")
    private int numberOfLikes;

    @JsonProperty("number_of_comments")
    private int numberOfComments;
}
