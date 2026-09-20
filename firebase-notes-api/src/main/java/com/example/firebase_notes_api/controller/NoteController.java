package com.example.firebase_notes_api.controller;

import com.example.firebase_notes_api.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

    @GetMapping("/{id}")
    public ResponseEntity<Map<String,Object>> getNote(@PathVariable String id) throws ExecutionException, InterruptedException {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.getNote(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String,Object>> updateNote(@PathVariable String id,@RequestBody Map<String,Object> noteDto) throws ExecutionException, InterruptedException {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.updateNote(id,noteDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable String id){
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

}
