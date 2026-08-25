package com.example.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDto {

    @NotBlank
    @Size(max = 100)
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
