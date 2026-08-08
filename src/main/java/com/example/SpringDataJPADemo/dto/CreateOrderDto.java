package com.example.SpringDataJPADemo.dto;

import com.example.SpringDataJPADemo.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CreateOrderDto {
    private String productName;
}
