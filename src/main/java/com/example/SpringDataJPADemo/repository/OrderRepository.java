package com.example.SpringDataJPADemo.repository;

import com.example.SpringDataJPADemo.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);
//    "Spring Data JPA can automatically implement repository methods when the method follows its
//    supported naming conventions, or when I explicitly tell it the query with @Query."
/*
* custom jpql --when derived queries are not enough
* jpql - works on objects not on tables
*
* we use "native sql" when "jpql" is not enough
* */
}
