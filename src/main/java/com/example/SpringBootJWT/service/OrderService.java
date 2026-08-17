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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private User getLoggedInUser() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getName();

        //debug
        System.out.println("getLoggedInUser: "+ email);
        System.out.println("getLoggedInUser: "+ SecurityContextHolder.getContext());
        System.out.println("getLoggedInUser: "+ SecurityContextHolder.getContext().getAuthentication());
        //debug

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public OrderDto map(Order savedOrder){
        return new OrderDto(savedOrder.getId(),savedOrder.getProductName(),
                new UserDto(savedOrder.getUser().getId(),savedOrder.getUser().getName(),savedOrder.getUser().getEmail()));

    }

    @Transactional
    public OrderDto createOrder(CreateOrderDto createOrderDto){
        /*
        here Transactional handles to trx first check whether user exist or not then save the
        created order in the database it ensure atomicity here
         */

        User user = getLoggedInUser();
        Order order = new Order();
        order.setUser(user);
        order.setProductName(createOrderDto.getProductName());
        Order savedOrder = orderRepository.save(order);
        return map(savedOrder);
    }

    public List<OrderDto> getMyOrders() {
        User user = getLoggedInUser();

        return orderRepository.findByUserId(user.getId())
                .stream()
                .map(this::map)
                .toList();
    }

    public OrderDto getMyOrder(Long id) {
        User user = getLoggedInUser();
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if(!order.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Access denied");
        }

        return map(order);

    }

    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> orderDtos = new ArrayList<>();
        for(Order order:orders){
            orderDtos.add(map(order));
        }
        return orderDtos;
    }

    public List<OrderDto> getOrdersByUser(Long userid) {
        List<Order> orders = orderRepository.findByUserId(userid);
        List<OrderDto> orderDtos = new ArrayList<>();
        for(Order order : orders){
            orderDtos.add(map(order));
        }
        return orderDtos;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
