package com.example.firebase_notes_api.controller;

import com.example.firebase_notes_api.dto.CreateNotesDto;
import com.example.firebase_notes_api.dto.NoteResponseDto;
import com.example.firebase_notes_api.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<NoteResponseDto> createNote(@Valid @RequestBody CreateNotesDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                noteService.createNote(dto));
    }

    @GetMapping
    public ResponseEntity<List<NoteResponseDto>> getAllNotes() {

        return ResponseEntity.ok(noteService.getAllNotes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponseDto> getNote(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.getNote(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponseDto> updateNote(@PathVariable String id,@Valid @RequestBody CreateNotesDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.updateNote(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

}
