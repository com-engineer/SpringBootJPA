package com.example.SpringDataJPADemo.repository;

import com.example.SpringDataJPADemo.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
//    `JpaRepository<User,Long> -->it takes two arguement 1.table name and 2.Type of the id

        @Override
    Page<User> findAll(Pageable pageable);
}
