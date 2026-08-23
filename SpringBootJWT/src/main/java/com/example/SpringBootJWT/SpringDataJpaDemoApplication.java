package com.example.SpringBootJWT;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringDataJpaDemoApplication {

	public static void main(String[] args) {
		System.out.println("Users Password:" + new BCryptPasswordEncoder().encode("user123"));
		System.out.println("Admin Password:" + new BCryptPasswordEncoder().encode("adminpass"));

		SpringApplication.run(SpringDataJpaDemoApplication.class, args);
	}

}
