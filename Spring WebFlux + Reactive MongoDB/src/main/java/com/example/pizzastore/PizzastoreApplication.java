package com.example.pizzastore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PizzastoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzastoreApplication.class, args);
	}
}
