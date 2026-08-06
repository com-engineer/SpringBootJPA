package com.example.SpringDataJPADemo.controller;

import com.example.SpringDataJPADemo.dto.CreateUserDto;
import com.example.SpringDataJPADemo.dto.UserDto;
import com.example.SpringDataJPADemo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor//automatically generates a constructor with all the fields of the class as parameters.
@RequestMapping("/api/v1")
public class UserController {
    private UserService userService;

//    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserDto createUserDto){
////        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser()
//
//    }
}
