package com.example.firebase_notes_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNotesDto {

    @NotBlank
    @NotNull
    private String title;

    @NotBlank
    @NotNull
    private String content;
}
