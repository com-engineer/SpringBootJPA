package com.example.product.controller;

import com.example.product.dto.CreateProductDto;
import com.example.product.dto.ProductResponseDto;
import com.example.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getActiveCatalog(){
        return ResponseEntity.ok(productService.getActiveCatalog());
    }

    @GetMapping("/admin")
    public ResponseEntity<List<ProductResponseDto>> getCatalogForAdmin(){
        return ResponseEntity.ok(productService.getCatalogForAdmin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody CreateProductDto createProductDto){
        return ResponseEntity.ok(productService.create(createProductDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long id ,@Valid @RequestBody CreateProductDto dto){
        return ResponseEntity.ok(productService.update(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id){
        productService.deactivate(id);
        return  ResponseEntity.noContent().build();
    }


}
