package com.example.pizzastore.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Document(collection = "ordersdetails")
@Data
public class OrderDetails {
	
	@Id
	private String orderDetailId;

	@NotEmpty(message = "Order must contain at least one pizza")
	private List<Pizza> pizzas;

	@Positive(message = "Quantity must be positive")
	@NotNull
	private int quantity;
	
	@NotNull
	private List<Integer> pizzasID;

	@NotBlank(message = "Order status must not be blank")
	private String status; 
	
	@NotNull
	private LocalDateTime timestamp = LocalDateTime.now();

}
