package com.example.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CreateOrderDto {

    @NotNull(message = "ProductId is required")
    private Long productId;

}
