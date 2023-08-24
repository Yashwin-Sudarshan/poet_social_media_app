package com.example.poetvine.server.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BaseUserDto {

    @JsonProperty("profile_image_name")
    private String profileImageName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("number_of_followers")
    private int numberOfFollowers;

    @JsonProperty("number_of_users_following")
    private int numberOfUsersFollowing;

    @JsonProperty("bio")
    private String bio;

    @JsonProperty("number_of_poems_published")
    private int numberOfPoemsPublished;

    @JsonProperty("topics_written_about")
    private String[] topicsWrittenAbout;
}
