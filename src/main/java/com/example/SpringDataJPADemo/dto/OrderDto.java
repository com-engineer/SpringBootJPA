package com.example.SpringDataJPADemo.dto;

import com.example.SpringDataJPADemo.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private String productName;
    private User user;
}
