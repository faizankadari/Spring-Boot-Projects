package com.example.grocerystore.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.grocerystore.entity.GroceryItem;
import com.example.grocerystore.entity.Order;
import com.example.grocerystore.entity.OrderItem;
import com.example.grocerystore.service.GroceryItemService;
import com.example.grocerystore.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/admin/api")
public class AdminController {

	@Autowired
	private GroceryItemService groceryItemService;

	@Autowired
	private OrderService orderService;

	@GetMapping("/grocery-items")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<List<GroceryItem>> getAllGroceryItems() {
		log.info("Received request to fetch all grocery items");

		List<GroceryItem> items = groceryItemService.getAllGroceryItems();
		log.info("Request executed successfully", items.size());

		return new ResponseEntity<>(items, HttpStatus.OK);
	}

	@PostMapping("/grocery-items")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> addGroceryItem(@RequestBody GroceryItem groceryItem) {

		log.info("Adding new grocery item: {}" + groceryItem);
		groceryItemService.addGroceryItem(groceryItem);
		log.info("Grocery item added successfully");
		return new ResponseEntity<>("Item Added", HttpStatus.OK);
	}

	@DeleteMapping("/grocery-items/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> removeGroceryItem(@PathVariable Long id) {
		groceryItemService.removeGroceryItem(id);
		return new ResponseEntity<>("Item Deleted Successfully", HttpStatus.OK);
	}

	@PutMapping("/grocery-items/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<GroceryItem> updateGroceryItem(@PathVariable Long id, @RequestBody GroceryItem groceryItem) {
		GroceryItem existingItem = groceryItemService.getAllGroceryItems().stream()
				.filter(item -> item.getId().equals(id)).findFirst().orElse(null);
		if (existingItem != null) {
			existingItem.setName(groceryItem.getName());
			existingItem.setPrice(groceryItem.getPrice());
			existingItem.setQuantity(groceryItem.getQuantity());
			groceryItemService.updateGroceryItem(existingItem, id);
			return new ResponseEntity<>(existingItem, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/orders")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> bookGroceryItems(@RequestBody List<OrderItem> orderItems) {
		Order order = new Order();
		order.setOrderDate(new Date());
		orderService.addOrder(order);

		boolean allItemsProcessed = true;

		for (OrderItem item : orderItems) {
			item.setOrder(order);

			if (item.getGroceryItem() == null) {
				allItemsProcessed = false;
			} else {
				if (!orderService.addOrderItem(item)) {
					allItemsProcessed = false;
				}
			}
		}

		if (allItemsProcessed) {
			log.info("Order completed successfully");
			return new ResponseEntity<>("Order Completed Successfully", HttpStatus.CREATED);
		} else {
			log.info("Order failed due to insufficient quantity or missing items");
			return new ResponseEntity<>("Requested Quantity is more than available", HttpStatus.ACCEPTED);
		}
	}

}
