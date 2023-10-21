package com.example.poetvine.server.payload;

import com.example.poetvine.server.model.enumeration.VisibilityPreference;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditUserRequest {

    @NotBlank
    @Email
    private String email;

    private String profileImageName;

    private String bio;

    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String password;

    private String[] topicsWrittenAbout;

    @NotNull
    private VisibilityPreference profileVisibilityPreference;

    @NotNull
    private VisibilityPreference poemVisibilityPreference;
}
