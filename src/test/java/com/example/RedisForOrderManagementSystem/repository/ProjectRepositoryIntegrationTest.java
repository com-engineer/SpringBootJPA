package com.example.RedisForOrderManagementSystem.repository;

import com.example.RedisForOrderManagementSystem.entities.Product;
import com.example.RedisForOrderManagementSystem.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectRepositoryIntegrationTest extends
        AbstractIntegrationTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should save Product")
    void shouldSaveProduct(){
        Product product = new Product();
        product.setName("iPhone");
        product.setActive(true);
        product.setPrice(BigDecimal.valueOf(70000.0));

        Product savedProduct = productRepository.save(product);

        assertNotNull(savedProduct.getId());
        assertEquals("iPhone",savedProduct.getName());

    }

    @Test
    @DisplayName("Should return active product")
    void shouldReturnActiveProducts(){
        Product p1 = new Product();
        p1.setName("Mouse");
        p1.setPrice(BigDecimal.valueOf(2000));
        p1.setActive(true);

        Product p2 = new Product();
        p2.setName("Keyboard");
        p2.setPrice(BigDecimal.valueOf(1000));
        p2.setActive(true);

        productRepository.saveAll(
                List.of(p1,p2)
        );

        List<Product> products = productRepository.findAllByActiveTrueOrderByNameAsc();

        assertEquals(2,products.size());

        assertEquals("Keyboard",
                products.get(0).getName());
    }
}
