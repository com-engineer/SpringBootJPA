package com.example.product.service;

import com.example.product.dto.CreateProductDto;
import com.example.product.dto.ProductResponseDto;
import com.example.product.entity.Product;
import com.example.product.exception.DuplicateProductNameException;
import com.example.product.exception.ProductNotFoundException;
import com.example.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private ProductResponseDto map(Product product) {

        return new ProductResponseDto(product.getId(),product.getName(),product.getPrice(),product.getActive());
    }


    private boolean isAdmin() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

    }

    public List<ProductResponseDto> getActiveCatalog() {

        List<Product> product = productRepository.findAllByActiveTrueOrderByNameAsc();

        return product.stream()
                .map(this::map)
                .toList();

    }

    public List<ProductResponseDto> getCatalogForAdmin() {

        List<Product> product = productRepository.findAll();
        return product.stream()
                .sorted((a,b) -> a.getName().compareToIgnoreCase(b.getName()))
                .map(this::map)
                .toList();
    }

    public ProductResponseDto getById(Long id) {

        Product product = productRepository.findById(id).
                orElseThrow(() -> new ProductNotFoundException("No product with id: "+id));

        if(!product.getActive() && !isAdmin()){
                throw new ProductNotFoundException("No product with id: "+id);
        }

        return map(product);
    }

    @Transactional
    public ProductResponseDto create(@Valid CreateProductDto createProductDto) {

        String name = Objects.requireNonNull(createProductDto.getName().trim());
        if(productRepository.existsByNameIgnoreCase(name)){
            throw new DuplicateProductNameException("A product with this name already exists");
        }

        Product product = new Product();
        product.setName(createProductDto.getName());
        product.setPrice(createProductDto.getPrice());
        product.setActive(createProductDto.getActive() != null ? createProductDto.getActive() : true);

        return map(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDto update(Long id,@Valid CreateProductDto dto) {

        Product product = productRepository.findById(id).
                orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        String name = Objects.requireNonNull(dto.getName()).trim();
        if (productRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new DuplicateProductNameException("A product with this name already exists");
        }

        product.setName(name);
        product.setPrice(dto.getPrice());
        if (dto.getActive() != null) {
            product.setActive(dto.getActive());
        }

        return map(productRepository.save(product));
    }

    @Transactional
    public void deactivate(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: "+id));

        product.setActive(false);
    }


}
