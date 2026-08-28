package com.example.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderResponseDto {

    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal priceAtPurchase;
    private UserResponseDto user;

}
