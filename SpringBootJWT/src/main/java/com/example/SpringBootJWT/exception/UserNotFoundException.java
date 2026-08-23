package com.example.SpringBootJWT.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String errorMsg){
        super(errorMsg);
    }
}
