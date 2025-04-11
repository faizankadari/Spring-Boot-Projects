package com.example.pizzastore.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.pizzastore.model.OrderDetails;

public interface OrderDetailsRepository  extends ReactiveMongoRepository<OrderDetails, String> {
	
}
