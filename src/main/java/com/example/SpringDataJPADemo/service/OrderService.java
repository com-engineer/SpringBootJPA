package com.example.SpringDataJPADemo.service;

import com.example.SpringDataJPADemo.dto.CreateOrderDto;
import com.example.SpringDataJPADemo.dto.OrderDto;
import com.example.SpringDataJPADemo.entities.Order;
import com.example.SpringDataJPADemo.entities.User;
import com.example.SpringDataJPADemo.repository.OrderRepository;
import com.example.SpringDataJPADemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderDto createOrder(Long userId, CreateOrderDto createOrderDto){
        User user = userRepository.findById(userId).orElseThrow();
        Order order = new Order();
        order.setUser(user);
        order.setProductName(createOrderDto.getProductName());
        Order savedOrder = orderRepository.save(order);
        return new OrderDto(savedOrder.getId(),savedOrder.getProductName(),savedOrder.getUser());
    }

    public List<OrderDto> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderDto> orderDtos = new ArrayList<>();
        orders.forEach(order -> {
            OrderDto orderDto = new OrderDto(order.getId(),order.getProductName(),order.getUser());
            orderDtos.add(orderDto);
        });
        return orderDtos;
    }
}
