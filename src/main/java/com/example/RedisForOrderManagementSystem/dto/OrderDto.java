package com.example.RedisForOrderManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private Long ProductId;
    private String productName;
    private BigDecimal priceAtPurchase;
    private UserDto user;
}
