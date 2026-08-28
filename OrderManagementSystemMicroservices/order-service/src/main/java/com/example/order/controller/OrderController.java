package com.example.order.controller;

import com.example.order.dto.CreateOrderDto;
import com.example.order.dto.OrderResponseDto;
import com.example.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody CreateOrderDto dto){

        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponseDto>> getMyOrders(){
        return ResponseEntity.ok(orderService.getMyOrders());
    }

    @GetMapping("/my/{id}")
    public ResponseEntity<OrderResponseDto> getMyOrder(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getMyOrder(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUser(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getOrdersByUser(id));
    }

    public ResponseEntity<Void> deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}
