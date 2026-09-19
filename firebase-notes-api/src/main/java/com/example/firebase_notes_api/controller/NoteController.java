package com.example.firebase_notes_api.controller;

import com.example.firebase_notes_api.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<String> createNote(@RequestBody Map<String,Object> noteData)
        throws Exception{
        String noteId = noteService.createNote(noteData);
        return ResponseEntity.ok(
                "Note created successfully. ID: " + noteId);
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllNotes()
            throws Exception {

        return ResponseEntity.ok(noteService.getAllNotes());
    }
}
