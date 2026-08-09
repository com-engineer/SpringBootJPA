package com.example.SpringDataJPADemo.exception;

import com.example.SpringDataJPADemo.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
/*It tells Spring:

"This class will handle exceptions thrown from my REST controllers globally."
*
 */
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    /*
    This tells Spring:

"When a UserNotFoundException occurs, execute this method."
     */
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("USER_NOT_FOUND",ex.getMessage()));
    }
}
