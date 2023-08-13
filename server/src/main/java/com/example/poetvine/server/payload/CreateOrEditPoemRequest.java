package com.example.poetvine.server.payload;

import com.example.poetvine.server.model.enumeration.PoemStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrEditPoemRequest {

    @NotBlank
    @NotNull
    private String title;

    private String content;

    private String[] tags;

    // Can add custom validation later
    @NotNull
    private PoemStatus status;
}
