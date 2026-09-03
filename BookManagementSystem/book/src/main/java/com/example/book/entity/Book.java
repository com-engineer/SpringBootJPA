package com.example.book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "books")
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 200)
    private String name;

    @Column(nullable = false,length = 200)
    private String author;

    @Column(nullable = false,precision = 8,scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Boolean available;

    @Column(nullable = false,name = "published_date")
    private LocalDate publishedDate;
}
