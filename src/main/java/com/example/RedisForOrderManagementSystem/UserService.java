package com.example.RedisForOrderManagementSystem;


import com.example.RedisForOrderManagementSystem.dto.CreateUserDto;
import com.example.RedisForOrderManagementSystem.dto.UserDto;
import com.example.RedisForOrderManagementSystem.entities.User;
import com.example.RedisForOrderManagementSystem.exception.EmailAlreadyExistsException;
import com.example.RedisForOrderManagementSystem.exception.UserNotFoundException;
import com.example.RedisForOrderManagementSystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    //method to return logged in user
    private User getLoggedInUser() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getName();

        //debug
        System.out.println("getLoggedInUser: "+ email);
        System.out.println("getLoggedInUser: "+ SecurityContextHolder.getContext());
        System.out.println("getLoggedInUser: "+ SecurityContextHolder.getContext().getAuthentication());
        //debug

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    //method to return user data in the form of userdto
    public UserDto map(User user){
        return new UserDto(user.getId(),user.getName(),user.getEmail());

    }


    public UserDto getCurrentUser() {
        User user = getLoggedInUser();

        //debug
        System.out.println("getCurrentUser: "+user);
        //debug

        return map(user);
    }

    @CacheEvict(value = "users", key = "#result.id")
    @Transactional
    public UserDto updateCurrentUser(CreateUserDto dto) {
        User user = getLoggedInUser();
        if(!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())){
            throw new EmailAlreadyExistsException("Email is already in use");
        }
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return map(user);
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Cacheable(value = "users", key = "#id")
    public UserDto getUserById(Long id) {
        log.info("Getting user from DB for id {}", id);
        User user = userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("User not found with id: "+ id));
        return map(user);
    }

    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }
}
