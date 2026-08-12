package com.example.SpringDataJPADemo.controller;

import com.example.SpringDataJPADemo.dto.CreateOrderDto;
import com.example.SpringDataJPADemo.dto.OrderDto;
import com.example.SpringDataJPADemo.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
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
