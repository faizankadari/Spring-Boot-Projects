package com.example.pizzastore.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.pizzastore.model.Order;

public interface OrderRepository extends ReactiveMongoRepository<Order, String> {
	
}
