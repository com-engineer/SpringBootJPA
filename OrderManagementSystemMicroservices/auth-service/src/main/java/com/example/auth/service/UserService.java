package com.example.auth.service;

import com.example.auth.dto.CreateUserDto;
import com.example.auth.dto.UserResponseDto;
import com.example.auth.entity.User;
import com.example.auth.exception.EmailAlreadyExistsException;
import com.example.auth.exception.UserNotFoundException;
import com.example.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private UserResponseDto map(User loggedInUser) {
        return new UserResponseDto(loggedInUser.getId(),loggedInUser.getName(),loggedInUser.getEmail());
    }

    private User getLoggedInUser() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
    
    public UserResponseDto getMe() {
       return map(getLoggedInUser());
    }

    @Transactional
    public UserResponseDto updateMe(@Valid CreateUserDto dto) {
        User user = getLoggedInUser();

        if(!(user.getEmail().equals(dto.getEmail())) && userRepository.existsByEmail(dto.getEmail())){
            throw new EmailAlreadyExistsException("Email already in user");
        }

        user.setEmail(dto.getEmail());
        user.setName(dto.getName());

        return map(user);
    }

    public List<UserResponseDto> getUsers() {
        List<User> user = userRepository.findAll();
        List<UserResponseDto> userResponseDtos = new ArrayList<>();

        return user.stream()
                .map(this::map)
                .toList();
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: "+id)
        );

        return map(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userRepository.delete(user);
    }

}
