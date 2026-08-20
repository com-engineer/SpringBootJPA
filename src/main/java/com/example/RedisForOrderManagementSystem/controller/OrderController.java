package com.example.RedisForOrderManagementSystem.controller;


import com.example.RedisForOrderManagementSystem.dto.CreateOrderDto;
import com.example.RedisForOrderManagementSystem.dto.OrderDto;
import com.example.RedisForOrderManagementSystem.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/orders")//with jwt
//@RequestMapping("/api/v1/users/{userId}/orders")//without jwt //    considering the fact that order cannot exits without users
public class OrderController {
    private final OrderService orderService;

    //USER -> create
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(dto));
    }
    /*
      // USER -> create
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderDto dto) {
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

     */
    //USER -> my orders
    @GetMapping("/my")
    public ResponseEntity<List<OrderDto>> getMyOrders(){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getMyOrders());
    }

    //USER -> specific order
    @GetMapping("/my/{id}")
    public ResponseEntity<OrderDto> getMyOrder(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getMyOrder(id));
    }

    //ADMIN -> all orders
    @GetMapping()
    public ResponseEntity<List<OrderDto>> getAllOrders(){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders());
    }

    //ADMIN -> orders by user
    @GetMapping("/user/{id}")
    public ResponseEntity<List<OrderDto>> getOrdersByUser(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrdersByUser(id));
    }

    //ADMIN -> delete
    @DeleteMapping("/{id}")
    public ResponseEntity<List<OrderDto>> deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
