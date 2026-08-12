package com.example.SpringBootJWT.controller;

import com.example.SpringBootJWT.dto.CreateOrderDto;
import com.example.SpringBootJWT.dto.OrderDto;
import com.example.SpringBootJWT.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
//@RequestMapping("/api/v1/orders")//in normal scenerio
//@RequestMapping("/api/v1/users/{userId}/orders")//without jwt //    considering the fact that order cannot exits without users
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@PathVariable Long userId, @RequestBody CreateOrderDto createOrderDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(userId,createOrderDto));
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrdersByUserId(@PathVariable Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrdersByUserId(userId));
    }
}
