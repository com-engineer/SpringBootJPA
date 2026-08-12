package com.example.SpringBootJWT.repository;

import com.example.SpringBootJWT.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
//    `JpaRepository<User,Long> -->it takes two arguement 1.table name and 2.Type of the id

        @Override
    Page<User> findAll(Pageable pageable);

//        @Override
//    Optional<User> findByEmail(String email);
    Optional<User> findByEmail(String email);
}
