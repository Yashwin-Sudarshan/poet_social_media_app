package com.example.poetvine.server.response;

import com.example.poetvine.server.annotation.CustomDateTimeFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LikeDto {

    @JsonProperty("like_id")
    private Long likeId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("poem_id")
    private Long poemId;

    @JsonProperty("liked_at")
    @CustomDateTimeFormat
    private LocalDateTime likedAt;
}
