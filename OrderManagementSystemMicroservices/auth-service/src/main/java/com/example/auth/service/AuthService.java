package com.example.auth.service;

import com.example.auth.dto.CreateUserDto;
import com.example.auth.dto.LoginDto;
import com.example.auth.dto.LoginResponseDto;
import com.example.auth.dto.RegisterUserResponseDto;
import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import com.example.auth.exception.EmailAlreadyExistsException;
import com.example.auth.exception.UserNotFoundException;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public RegisterUserResponseDto registerUser(@Valid CreateUserDto createUserDto) {

        if(userRepository.existsByEmail(createUserDto.getEmail())){
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        User user = new User();
        user.setEmail(createUserDto.getEmail());
        user.setName(createUserDto.getName());
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return new RegisterUserResponseDto(savedUser.getName(), savedUser.getId());

    }

    public LoginResponseDto login(@Valid LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getPassword(),loginDto.getEmail())
        );

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found after authentication"));

        return new LoginResponseDto(jwtService. getJwtToken(user));
    }
}
