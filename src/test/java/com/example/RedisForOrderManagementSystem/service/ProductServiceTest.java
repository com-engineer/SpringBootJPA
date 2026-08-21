package com.example.RedisForOrderManagementSystem.service;

import com.example.RedisForOrderManagementSystem.ProductService;
import com.example.RedisForOrderManagementSystem.dto.ProductDto;
import com.example.RedisForOrderManagementSystem.dto.ProductRequestDto;
import com.example.RedisForOrderManagementSystem.entities.Product;
import com.example.RedisForOrderManagementSystem.exception.DuplicateProductNameException;
import com.example.RedisForOrderManagementSystem.exception.ProductNotFoundException;
import com.example.RedisForOrderManagementSystem.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldReturnProductById() {
//   2nd step     Arrange
        Product product = new Product();
        product.setActive(true);
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(BigDecimal.valueOf(50000.0));

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

//     1st step   Action
        ProductDto actualProduct = productService.getById(1L);

//      3rd step Assert
        assertThat(actualProduct.getName()).isEqualTo("Laptop");
        assertThat(actualProduct.getId()).isEqualTo(1L);

//        to check whether it is actually called or not
//        verify(productRepository).findById(1L);
        verify(productRepository,times(1)).findById(1L);

    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
//   2nd step     Arrange
        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

//      3rd step Assert
        assertThatThrownBy(() -> productService.getById(1L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("No Product with Id: 1");

        verify(productRepository,times(1)).findById(1L);
    }


    @Test
    void shouldReturnActiveCatalog() {
        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("Phone");
        p1.setPrice(BigDecimal.valueOf(50000.0));
        p1.setActive(true);

        Product p2 = new Product();
        p2.setId(2L);
        p2.setName("Laptop");
        p2.setPrice(BigDecimal.valueOf(30000.0));
        p2.setActive(true);

        when(productRepository.findAllByActiveTrueOrderByNameAsc())
                .thenReturn(List.of(p1,p2));

        List<ProductDto> result = productService.getActiveCatalog();

        assertThat(result).hasSize(2);

        assertThat(result.get(0).getName()).isEqualTo("Phone");

        verify(productRepository).findAllByActiveTrueOrderByNameAsc();
    }

    @Test
    void shouldCreateProduct() {
        ProductRequestDto dto =
                new ProductRequestDto("Laptop",BigDecimal.valueOf(50000.0),true);

        when(productRepository.existsByNameIgnoreCase("Laptop"))
                .thenReturn(false);

        Product product = new Product();
        product.setActive(true);
        product.setPrice(BigDecimal.valueOf(50000.0));
        product.setName("Laptop");
        product.setId(1L);

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        ProductDto result = productService.create(dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Laptop");

        verify(productRepository).existsByNameIgnoreCase("Laptop");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenProductNameAlreadyExists() {

        ProductRequestDto dto =
                new ProductRequestDto(
                        "Laptop",
                        BigDecimal.valueOf(50000.0),
                        true
                );

        when(productRepository.existsByNameIgnoreCase("Laptop"))
                .thenReturn(true);

        assertThatThrownBy(() ->
                productService.create(dto))
                .isInstanceOf(DuplicateProductNameException.class)
                .hasMessage("A product with this name already exists: Laptop");

        verify(productRepository)
                .existsByNameIgnoreCase("Laptop");

        verify(productRepository,never())
                .save(any());

    }

    @Test
    void shouldUpdateProduct() {

        Product existing = new Product();
        existing.setId(1L);
        existing.setName("Old Laptop");
        existing.setPrice(BigDecimal.valueOf(40000.0));
        existing.setActive(true);

        ProductRequestDto dto =
                new ProductRequestDto("New Laptop",BigDecimal.valueOf(55000.0),false);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(productRepository.existsByNameIgnoreCaseAndIdNot(
                "New Laptop",
                1L))
                .thenReturn(false);

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProductDto result =
                productService.update(1L,dto);

        assertThat(result.getName()).isEqualTo("New Laptop");

        assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(55000.0));

        assertThat(result.isActive()).isFalse();

    }

    @Test
    void shouldDeactivateProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setActive(true);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        productService.deactivate(1L);

        assertThat(product.isActive()).isFalse();

        verify(productRepository).findById(1L);
    }
}