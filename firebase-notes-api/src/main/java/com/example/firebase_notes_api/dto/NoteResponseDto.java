package com.example.firebase_notes_api.dto;

import com.google.cloud.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class NoteResponseDto {

    private String id;
    private String title;
    private Instant createdAt;
    private String content;
}
