package com.example.SpringDataJPADemo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity//tells Hibernate:"This Java class represents a table in the database."
@Table(name = "users")
@Getter
//Lombok is a Java library that automatically generates boilerplate code (repetitive code) like:
//
//Getters
//        Setters
//Constructors
//toString()
//equals()
//hashCode()
//Builder pattern
@Setter
public class User {//User is a reserved word in hibernate ->"unquoted identifiers are automatically converted to lowercase."
    @Id//tells Hibernate, "This field uniquely identifies each row in the table."
    @GeneratedValue(strategy = GenerationType.IDENTITY)//tells Hibernate:"Don't ask me for the ID. Let the database generate it automatically when a new row is inserted."
    private Long id;
    private String name;
    @Column(unique = true,nullable = false)//while using jwt
    private String email;
    //while using jwt add new fields
    @Column(nullable = false)
    private String  password;
    @Enumerated(EnumType.STRING)
    private Role role;

//    @OneToMany
//    private List<Order> orders;

}
