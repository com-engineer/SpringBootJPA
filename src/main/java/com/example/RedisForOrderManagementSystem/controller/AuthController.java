package com.example.RedisForOrderManagementSystem.controller;


import com.example.RedisForOrderManagementSystem.dto.CreateUserDto;
import com.example.RedisForOrderManagementSystem.dto.LoginDto;
import com.example.RedisForOrderManagementSystem.dto.LoginResponseDto;
import com.example.RedisForOrderManagementSystem.dto.RegisterUserDto;
import com.example.RedisForOrderManagementSystem.AuthService;
import jakarta.validation.Valid;
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
    public ResponseEntity<RegisterUserDto>  registerUser(@Valid @RequestBody CreateUserDto createUserDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(createUserDto));
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto){

        //debug
        System.out.println("login(@RequestBody: "+loginDto);
        //debug

        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginDto));
    }


}
