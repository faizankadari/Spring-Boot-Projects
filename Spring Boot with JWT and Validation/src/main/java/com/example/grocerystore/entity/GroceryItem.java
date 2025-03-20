package com.example.grocerystore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity

@Table(name = "grocerystore_items")
@Data
public class GroceryItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "Item name cannot be empty")
	private String name;

	@NotBlank(message = "price cannot be empty")
	@Min(value = 0, message = "price must be greater than 0")
	@Max(value = 1000, message = "price should be less than 1000")
	private Double price;
	
	@NotBlank(message = "quantity cannot be empty")
	@Min(value = 0, message = "quantity must be greater than 0")
	@Max(value = 1000, message = "quantity should be less than 1000")
	private Integer quantity;
}