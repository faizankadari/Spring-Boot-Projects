package com.example.pizzastore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pizzastore.model.Pizza;
import com.example.pizzastore.service.PizzaService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("/api/pizzas")
public class PizzaController {

	@Autowired
	private PizzaService pizzaService;

	// Create a new pizza
	@PostMapping
	public ResponseEntity<Mono<Pizza>> createPizza(@Valid @RequestBody Pizza pizza) {
		log.info("Entering createPizza method in controller with pizza id: " + pizza.getId());
		return new ResponseEntity<>(pizzaService.createPizza(pizza), HttpStatus.OK);
	}

	// Get a pizza by ID
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Pizza>> getPizzaById(@PathVariable Integer id) {
		log.info("Entering getPizzaById method in controller with pizza id: " + id);
		return pizzaService.getPizzaById(id).map(pizza -> ResponseEntity.ok(pizza))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	// Get all pizzas
	@GetMapping
	public Flux<Pizza> getAllPizzas() {
		log.info("Entering getAllPizzas method in controller");
		return pizzaService.getAllPizzas();
	}

	// Get pizzas by name
	@GetMapping("/search")
	public Flux<Pizza> getPizzaByName(@RequestParam String name) {
		log.info("Entering getPizzaByName method in controller with pizza name: " + name);
		return pizzaService.getPizzaByName(name);
	}

	// Update an existing pizza
	@PutMapping("/{id}")
	public Mono<ResponseEntity<Pizza>> updatePizza(@PathVariable Integer id, @Valid @RequestBody Pizza pizza) {
		return pizzaService.updatePizza(id, pizza).map(updatedPizza -> ResponseEntity.ok(updatedPizza))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	// Delete Pizza by ID
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<String>> deletePizza(@PathVariable Integer id) {
		log.info("Entering deletePizza method in controller with pizza id: " + id);
		return pizzaService.deletePizza(id).then(Mono.just(ResponseEntity.ok("Pizza Deleted Successfully")))
				.switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pizza not found")));
	}

}
