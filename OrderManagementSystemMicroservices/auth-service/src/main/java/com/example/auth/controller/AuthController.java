package com.example.auth.controller;

import com.example.auth.dto.CreateUserDto;
import com.example.auth.dto.LoginDto;
import com.example.auth.dto.LoginResponseDto;
import com.example.auth.dto.RegisterUserResponseDto;
import com.example.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    public final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDto> registerUser(@Valid @RequestBody CreateUserDto createUserDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(createUserDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto){
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginDto));
    }
}
