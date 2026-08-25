package com.example.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {

    private String code;
    private String message;

}
