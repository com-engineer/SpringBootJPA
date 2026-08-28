package com.example.order.service;

import com.example.order.client.ProductClient;
import com.example.order.dto.CreateOrderDto;
import com.example.order.dto.OrderResponseDto;
import com.example.order.dto.ProductDto;
import com.example.order.dto.UserResponseDto;
import com.example.order.entity.Order;
import com.example.order.exception.OrderAccessDeniedException;
import com.example.order.exception.OrderNotFoundException;
import com.example.order.kafka.OrderEventPublisher;
import com.example.order.repository.OrderRepository;
import com.example.order.security.JwtPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductClient productClient;
    private final HttpServletRequest httpServletRequest;
    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    private JwtPrincipal getLoggedInPrincipal() {
        Object principal = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        if(principal instanceof JwtPrincipal jwtPrincipal){
            return jwtPrincipal;
        }
        throw new OrderAccessDeniedException("Authentication required");
    }

    private Object getBearerToken() {
        String header = httpServletRequest.getHeader("Authorization");

        if(header != null && header.startsWith("Bearer ")){
            return header.substring(7);
        }

        throw new OrderAccessDeniedException("Missing authorization token");
    }

    private OrderResponseDto map(Order saved) {

        return new OrderResponseDto(saved.getId(), saved.getProductId(), saved.getProductName(), saved.getPriceAtPurchase(),
                new UserResponseDto(saved.getUserId(), saved.getUserName(), saved.getUserEmail()));

    }

    @Transactional
    public OrderResponseDto createOrder(@Valid CreateOrderDto dto) {
        JwtPrincipal jwtPrincipal = getLoggedInPrincipal();
        ProductDto product = productClient.getProduct(dto.getProductId(),getBearerToken());

        Order order = new Order();
        order.setUserId(jwtPrincipal.userId());
        order.setUserName(jwtPrincipal.name());
        order.setUserEmail(jwtPrincipal.email());
        order.setProductId(product.getId());
        order.setProductName(product.getName());
        order.setPriceAtPurchase(product.getPrice());

        Order saved = orderRepository.save(order);
        orderEventPublisher.publishOrderCreated(saved);
        return map(saved);
    }

    public List<OrderResponseDto> getMyOrders() {
        JwtPrincipal principal = getLoggedInPrincipal();
        String email = principal.email();
        List<Order> orders = orderRepository.findAllByEmail(email);

        return orders.stream()
                .map(this::map)
                .toList();
    }

    public OrderResponseDto getMyOrder(Long id) {
        JwtPrincipal principal = getLoggedInPrincipal();
        Long userId = principal.userId();

        Order order = orderRepository.findById(id).
                orElseThrow(() -> new OrderNotFoundException("Order not found with id: "+id));

        if(!order.getUserId().equals(userId)){
            throw new OrderNotFoundException("Order not found with userId: "+userId);
        }

        return map(order);
    }

    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    public List<OrderResponseDto> getOrdersByUser(Long id) {

        return orderRepository.findByUserId(id).stream()
                .map(this::map)
                .toList();

    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new OrderNotFoundException("Order not found with id: "+id)
        );
        orderRepository.delete(order);

    }


}
