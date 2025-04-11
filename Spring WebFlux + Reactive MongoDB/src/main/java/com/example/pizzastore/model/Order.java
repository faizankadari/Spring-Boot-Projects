package com.example.pizzastore.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Document(collection = "orders")
@Data
public class Order {
	@Id
	private String orderId;

	@NotNull
	private List<Integer> pizzasID;
	
	@Positive(message = "Quantity must be positive")
	@NotNull
	private int quantity;
}
