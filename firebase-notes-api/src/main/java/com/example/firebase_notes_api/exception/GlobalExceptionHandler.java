package com.example.firebase_notes_api.exception;

import com.example.firebase_notes_api.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FirebaseOperationException.class)
    public ResponseEntity<ErrorResponseDto> handleFirebaseOperationException(FirebaseOperationException firebaseOperationException){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponseDto( "FIREBASE_OPERATION_ERROR",firebaseOperationException.getMessage())
        );
    }

    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNoteNotFoundException(NoteNotFoundException noteNotFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponseDto("NOTE_NOT_FOUND",noteNotFoundException.getMessage())
        );
    }
}
