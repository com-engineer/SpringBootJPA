package com.example.RedisForOrderManagementSystem.service;

import com.example.RedisForOrderManagementSystem.dto.CreateUserDto;
import com.example.RedisForOrderManagementSystem.dto.LoginDto;
import com.example.RedisForOrderManagementSystem.dto.LoginResponseDto;
import com.example.RedisForOrderManagementSystem.dto.RegisterUserDto;
import com.example.RedisForOrderManagementSystem.entities.Role;
import com.example.RedisForOrderManagementSystem.entities.User;
import com.example.RedisForOrderManagementSystem.exception.EmailAlreadyExistsException;

import com.example.RedisForOrderManagementSystem.repository.UserRepository;
import com.example.RedisForOrderManagementSystem.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    public RegisterUserDto registerUser(CreateUserDto createUserDto){
        if(userRepository.existsByEmail(createUserDto.getEmail())){
            throw new EmailAlreadyExistsException("Email is already registered");
        }
        User user =  new User();
        user.setEmail(createUserDto.getEmail());
        user.setName(createUserDto.getName());
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        return new RegisterUserDto(savedUser.getName(),savedUser.getId());
        }


    public LoginResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );

        //debug
        System.out.println("authentication: "+authentication);
        System.out.println("authentication: "+authentication.getPrincipal());
        System.out.println("authentication: "+authentication.getAuthorities());
        //debug

        String jwtToken = jwtService.generateJwtToken((UserDetails) Objects.requireNonNull(authentication.getPrincipal()));

        //debug
        System.out.println("CreatedjwtToken: "+jwtToken);
        //debug

        return new LoginResponseDto(jwtToken);

    }
}
