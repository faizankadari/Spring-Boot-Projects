package com.example.pizzastore.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.pizzastore.model.Pizza;

import reactor.core.publisher.Flux;

public interface PizzaRepository extends ReactiveMongoRepository<Pizza, Integer> {
    Flux<Pizza> findByName(String name);
}
