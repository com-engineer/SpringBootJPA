package com.example.SpringDataJPADemo.service;

import com.example.SpringDataJPADemo.dto.CreateUserDto;
import com.example.SpringDataJPADemo.dto.RegisterUserDto;
import com.example.SpringDataJPADemo.entities.Role;
import com.example.SpringDataJPADemo.entities.User;
import com.example.SpringDataJPADemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    public RegisterUserDto registerUser(CreateUserDto createUserDto){
        User user =  new User();
        user.setEmail(createUserDto.getEmail());
        user.setName(createUserDto.getName());
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        return new RegisterUserDto(savedUser.getName(),savedUser.getId());
        }


}
