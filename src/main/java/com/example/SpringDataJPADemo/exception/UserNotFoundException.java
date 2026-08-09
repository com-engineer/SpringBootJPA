package com.example.SpringDataJPADemo.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String errorMsg){
        super(errorMsg);
    }
}
