package com.example.RedisForOrderManagementSystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CreateOrderDto {
    @NotNull(message = "Porduct id is required")
    private Long productId;
}
