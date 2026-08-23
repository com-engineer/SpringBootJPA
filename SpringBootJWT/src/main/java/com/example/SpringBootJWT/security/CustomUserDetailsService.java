package com.example.SpringBootJWT.security;

import com.example.SpringBootJWT.exception.UserNotFoundException;
import com.example.SpringBootJWT.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        com.example.SpringBootJWT.entities.User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: "+ email));

        //debug
        System.out.println("UserDetailsService: "+user);
        System.out.println("email: "+user.getEmail()+" "+"password: "+user.getPassword()+" "+user.getRole()+" "+user.getRole().name());
        //debug

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
