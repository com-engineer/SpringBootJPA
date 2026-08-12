package com.example.SpringBootJWT.controller;

import com.example.SpringBootJWT.dto.CreateUserDto;
import com.example.SpringBootJWT.dto.LoginDto;
import com.example.SpringBootJWT.dto.LoginResponseDto;
import com.example.SpringBootJWT.dto.RegisterUserDto;
import com.example.SpringBootJWT.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    //creating user
    public ResponseEntity<RegisterUserDto>  registerUser(@RequestBody CreateUserDto createUserDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(createUserDto));
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto){
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginDto));
    }

}
