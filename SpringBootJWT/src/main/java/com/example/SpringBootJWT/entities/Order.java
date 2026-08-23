package com.example.SpringBootJWT.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;

    @ManyToOne(fetch = FetchType.LAZY)//lazy will fetch when required
    @JoinColumn(name = "user_id")//It tells Hibernate:"Store the User's id in a column called user_id."
    private User user;

}
