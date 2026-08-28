package com.example.order.security;

public record JwtPrincipal(Long userId,String name,String email,String role) {
}
