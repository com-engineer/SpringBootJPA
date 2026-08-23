package com.example.RedisForOrderManagementSystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDto {
    @NotBlank
    @NonNull
    @Size(max = 100)
    private  String name;

    @NotBlank
    @NonNull
    @Email
    private  String email;


    @NotBlank
    @NonNull
    private String password;
}
