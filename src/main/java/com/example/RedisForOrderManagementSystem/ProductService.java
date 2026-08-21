package com.example.RedisForOrderManagementSystem;

import com.example.RedisForOrderManagementSystem.dto.ProductDto;
import com.example.RedisForOrderManagementSystem.dto.ProductRequestDto;
import com.example.RedisForOrderManagementSystem.entities.Product;
import com.example.RedisForOrderManagementSystem.entities.User;
import com.example.RedisForOrderManagementSystem.exception.DuplicateProductNameException;
import com.example.RedisForOrderManagementSystem.exception.ProductNotFoundException;
import com.example.RedisForOrderManagementSystem.repository.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j //automatically creates a logger for your class, conceptually like:
                                            /*
                                            private static final Logger log =
                                                    LoggerFactory.getLogger(ProductService.class);
                                             */
public class ProductService {

    private final ProductRepository productRepository;

    public ProductDto map(Product product){
        return new ProductDto(product.getId(),product.getName(),
                product.getPrice(),product.isActive());
    }

    private boolean isAdmin() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)//["ROLE_USER", "ROLE_ADMIN"] -> Each item is a GrantedAuthority object.
                .anyMatch("ROLE_ADMIN"::equals);
    }



    public List<ProductDto> getActiveCatalog() {
        List<Product> products = productRepository.findAllByActiveTrueOrderByNameAsc();
        List<ProductDto> productDtos = products.stream().map(this::map).toList();
        return productDtos;
    }

    public List<ProductDto> getAllForAdmin() {
        return productRepository.findAll().stream()
                .sorted((a,b) -> (a.getName()).compareToIgnoreCase(b.getName()))
                .map(this::map)
                .toList();
    }

    @Cacheable(value = "products",key = "#id")
    public ProductDto getById(Long id) {

        log.info("Getting product from DB for id {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("No Product with Id: "+id));

        if(!product.isActive() && !isAdmin()){
            throw  new ProductNotFoundException("No Product with Id: "+id);
        }
        return map(product);
    }


    public ProductDto create(@Valid ProductRequestDto dto) {
        String name = Objects.requireNonNull(dto.getName());
        if(productRepository.existsByNameIgnoreCase(name)){
            throw new DuplicateProductNameException("A product with this name already exists: "+name);
        }
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setActive((dto.getActive() != null )? dto.getActive() : true );

        Product savedProduct = productRepository.save(product);
        return map(savedProduct);
    }

//    @CacheEvict(value = "products", key = "#id")
    @CachePut(value = "products", key = "#id")
    @Transactional
    public ProductDto update(Long id, @Valid ProductRequestDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("No Product with Id: +id"));
        String name = Objects.requireNonNull(dto.getName());
        if(productRepository.existsByNameIgnoreCaseAndIdNot(name,id)){
            throw new DuplicateProductNameException("A product with this name already exists: "+name);
        }
        product.setActive(dto.getActive() != null ? dto.getActive() : true);
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());

        Product savedProduct = productRepository.save(product);
        return map(savedProduct);
    }

    @CacheEvict(value = "products", key = "#id")
    @Transactional
    public void deactivate(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("No product with id " + id));
        product.setActive(false);
    }

    

}
