package com.example.pizzastore.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Document(collection = "pizzas")
@Data
public class Pizza {

	@Id
	private Integer id;
	
	@NotBlank(message = "Pizza name must not be blank")
	private String name;

	private String description;

	@NotNull(message = "Toppings list must not be null")
	private List<String> toppings;

	@NotBlank(message = "Size must be Small or Medium or Large")
	private String size;

	@NotNull
	@Positive(message = "Price must be positive")
	private double price;
	
	@Positive(message = "Quantity must be positive")
	private int quantity;

}
