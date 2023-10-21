package com.example.poetvine.server.response;

import com.example.poetvine.server.model.enumeration.VisibilityPreference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MyUserDto extends BaseUserDto {

    private String email;

    @JsonProperty("number_of_poems_saved")
    private int numberOfPoemsSaved;

    @JsonProperty("profile_visibility_preference")
    private VisibilityPreference profileVisibilityPreference;

    @JsonProperty("poem_visibility_preference")
    private VisibilityPreference poemVisibilityPreference;
}
