package com.example.RedisForOrderManagementSystem.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ProductRequestDto {
    @NotBlank(message = "name is required")
    @Size(max = 200,message = "name must be atmost 200 characters")
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01",message = "price must be at least 0.01")
    private BigDecimal price;

    private Boolean active;

}
