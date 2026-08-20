package com.example.RedisForOrderManagementSystem.service;


import com.example.RedisForOrderManagementSystem.dto.CreateOrderDto;
import com.example.RedisForOrderManagementSystem.dto.OrderDto;
import com.example.RedisForOrderManagementSystem.dto.UserDto;
import com.example.RedisForOrderManagementSystem.entities.Order;
import com.example.RedisForOrderManagementSystem.entities.Product;
import com.example.RedisForOrderManagementSystem.entities.User;
import com.example.RedisForOrderManagementSystem.exception.*;
import com.example.RedisForOrderManagementSystem.repository.OrderRepository;
import com.example.RedisForOrderManagementSystem.repository.ProductRepository;
import com.example.RedisForOrderManagementSystem.repository.UserRepository;
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
    private final ProductRepository productRepository;

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

    public OrderDto map(Order order){
        User user = order.getUser();
        Product product = order.getProduct();
        return new OrderDto(order.getId(),product.getId(),product.getName(),order.getPriceAtPurchase(),
                new UserDto(user.getId(),user.getName(),user.getEmail()));

    }

    @Transactional
    public OrderDto createOrder(CreateOrderDto createOrderDto){
        User user = getLoggedInUser();
        Product product = productRepository.findById(createOrderDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("No product with id " + createOrderDto.getProductId()));
        //cannot create order if product is not active
        if(!product.isActive()){
            throw new ProductInactiveException("Product is not available for ordering");
        }
        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setPriceAtPurchase(product.getPrice());
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
                .orElseThrow(() -> new OrderNotFoundException("No order with id " + id));

        if(!order.getUser().getId().equals(user.getId())){
            throw new OrderAccessDeniedException("You do not have access to this order");
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
        Order order = orderRepository.findById(id)
                        .orElseThrow(() -> new ProductNotFoundException("No order with id " + id));
        orderRepository.delete(order);
    }
}
