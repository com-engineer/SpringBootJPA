package com.example.SpringBootJWT.service;

import com.example.SpringBootJWT.dto.CreateOrderDto;
import com.example.SpringBootJWT.dto.OrderDto;
import com.example.SpringBootJWT.dto.UserDto;
import com.example.SpringBootJWT.entities.Order;
import com.example.SpringBootJWT.entities.User;
import com.example.SpringBootJWT.exception.UserNotFoundException;
import com.example.SpringBootJWT.repository.OrderRepository;
import com.example.SpringBootJWT.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderDto createOrder(Long userId, CreateOrderDto createOrderDto){
        /*
        here Transactional handles to trx first check whether user exist or not then save the
        created order in the database it ensure atomicity here
         */

        User user = userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException("User not found with id: "+ userId));
        Order order = new Order();
        order.setUser(user);
        order.setProductName(createOrderDto.getProductName());
        Order savedOrder = orderRepository.save(order);
        return new OrderDto(savedOrder.getId(),savedOrder.getProductName(),
                new UserDto(savedOrder.getUser().getId(),savedOrder.getUser().getName(),savedOrder.getUser().getEmail()));
    }

    public List<OrderDto> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderDto> orderDtos = new ArrayList<>();
        orders.forEach(order -> {
            OrderDto orderDto = new OrderDto(order.getId(),order.getProductName(),
                    new UserDto(order.getUser().getId(),order.getUser().getName(),order.getUser().getEmail()));
            orderDtos.add(orderDto);
        });
        return orderDtos;
    }
}
