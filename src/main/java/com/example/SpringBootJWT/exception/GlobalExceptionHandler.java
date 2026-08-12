package com.example.SpringBootJWT.exception;

import com.example.SpringBootJWT.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    /*
    This tells Spring:

"When a UserNotFoundException occurs, execute this method."
     */
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
//        ex.getBindingResult().getFieldError();//returns a list
        //trying to understand what is present inside the list so we create a hashmap and store the list
        HashMap<String,String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            fieldErrors.put(error.getField(),error.getDefaultMessage());
        });
        StringBuilder errorMsg = new StringBuilder();
        boolean isFirst = true;
        for(String field: fieldErrors.keySet()){
            if(!isFirst){
                errorMsg.append(", ");
            }
            isFirst = false;
            errorMsg.append(field).append(" : ").append(fieldErrors.get(field));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body(new ErrorResponseDto("INVALID_INPUT",errorMsg.toString()));
//                body(new ErrorResponseDto("INVALID_INPUT",ex.getBindingResult().getFieldErrors().toString()));
    }


}
