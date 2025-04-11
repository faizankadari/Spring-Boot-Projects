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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.pizzastore.model.Order;
import com.example.pizzastore.model.OrderDetails;
import com.example.pizzastore.service.OrderService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	// Create a new order
	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public Mono<Object> createOrder(@Valid @RequestBody Order order) {
		return orderService.createOrder(order);
	}

	// Get an order by ID
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<ResponseEntity<OrderDetails>> getOrderById(@PathVariable String id) {
		return orderService.getOrderById(id).map(order -> ResponseEntity.ok(order))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	// Get all orders
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Flux<OrderDetails> getAllOrders() {
		return orderService.getAllOrders();
	}

	// Update the status of an existing order
	@PutMapping("/{id}/status")
	@ResponseStatus(HttpStatus.OK)
	public Mono<ResponseEntity<OrderDetails>> updateOrderStatus(@PathVariable String id) {
		return orderService.updateOrderStatus(id).map(updatedOrder -> ResponseEntity.ok(updatedOrder))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	// Delete an order by ID
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<ResponseEntity<String>> cancelOrder(@PathVariable String id) {
		return orderService.cancelOrder(id).
				then(Mono.just(ResponseEntity.ok("Order Canceled Successfully"))).
				switchIfEmpty(Mono.error(new RuntimeException("Order not found")));
	}
}
