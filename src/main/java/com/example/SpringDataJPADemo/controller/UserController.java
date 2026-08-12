package com.example.SpringDataJPADemo.controller;

import com.example.SpringDataJPADemo.dto.CreateUserDto;
import com.example.SpringDataJPADemo.dto.UserDto;
import com.example.SpringDataJPADemo.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor//automatically generates a constructor with all the fields of the class as parameters.
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

//    @PostMapping
////    without using jwt
//    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserDto createUserDto){
//        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(createUserDto));
//
//    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
    }

    @GetMapping("/users/paginated")
    public ResponseEntity<List<UserDto>> getUsersPaginated(@RequestParam int page,@RequestParam int pageSize,
                                                           @RequestParam(defaultValue = "asc") String direction,
                                                           @RequestParam(defaultValue = "name") String sortBy){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsersPaginated(page,pageSize,direction,sortBy));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        .build()
//
//which simply means
//
//"Create the ResponseEntity with the status and headers, but without any body."
    }
    @PatchMapping("/users/{id}")//PATCH → Update only the fields that have changed.
    public ResponseEntity<UserDto> patchUser(@PathVariable Long id,@RequestBody CreateUserDto patchUserDto){
        return ResponseEntity.status(HttpStatus.OK).body(userService.patchUser(id,patchUserDto));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,@RequestBody CreateUserDto updateUserDto){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id,updateUserDto));
    }

//    get orders for the users


}
