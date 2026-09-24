package com.example.firebase_notes_api.exception;

public class FirebaseOperationException extends RuntimeException {
    public FirebaseOperationException(String message) {

        super(message);
    }
    public FirebaseOperationException(String message, Throwable cause) {
        super(message, cause.getCause());
    }
}
