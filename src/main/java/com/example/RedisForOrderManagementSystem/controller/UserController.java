package com.example.RedisForOrderManagementSystem.controller;


import com.example.RedisForOrderManagementSystem.dto.CreateUserDto;
import com.example.RedisForOrderManagementSystem.dto.UserDto;
import com.example.RedisForOrderManagementSystem.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor//automatically generates a constructor with all the fields of the class as parameters.
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

//    @PostMapping
////    without using jwt
//    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserDto createUserDto){
//        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(createUserDto));
//
//    }

    //USER -> own profile
    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getCurrentUser());
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMe(@RequestBody CreateUserDto dto){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateCurrentUser(dto));
    }

    //ADMIN -> All users
    @GetMapping()
    public ResponseEntity<List<UserDto>> getUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
    }

    //ADMIN -> Specific user
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
    }

    //ADMIN -> delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
