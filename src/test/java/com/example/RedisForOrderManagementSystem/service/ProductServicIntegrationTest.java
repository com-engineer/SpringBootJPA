package com.example.RedisForOrderManagementSystem.service;

import com.example.RedisForOrderManagementSystem.AbstractIntegrationTest;
import com.example.RedisForOrderManagementSystem.ProductService;
import com.example.RedisForOrderManagementSystem.dto.ProductDto;
import com.example.RedisForOrderManagementSystem.dto.ProductRequestDto;
import com.example.RedisForOrderManagementSystem.entities.Product;
import com.example.RedisForOrderManagementSystem.exception.DuplicateProductNameException;
import com.example.RedisForOrderManagementSystem.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductServicIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("should create product")
    void shouldCreateProduct(){

        ProductRequestDto dto = new ProductRequestDto(
                "Laptop",BigDecimal.valueOf(70000),
                true
        );

//        dto.setName("Laptop");
//        dto.setPrice(BigDecimal.valueOf(70000));

        ProductDto result = productService.create(dto);

        assertNotNull(result.getId());

        Product dbProduct = productRepository.findById(result.getId())
                .orElseThrow();

        assertEquals("Laptop",
                dbProduct.getName());
    }

    @Test
    @DisplayName("Should throw duplicate exception")
    void shouldThrowDuplicateException(){
        Product product = new Product();

        product.setName("iPhone");
        product.setPrice(BigDecimal.valueOf(100000));

        productRepository.save(product);

        ProductRequestDto dto = new ProductRequestDto(
                "iPhone",
                BigDecimal.valueOf(100000),
                true
        );

        dto.setName("iPhone");
        dto.setPrice(BigDecimal.valueOf(100000));

        assertThrows(
                DuplicateProductNameException.class,
                () -> productService.create(dto)
        );
    }
}
